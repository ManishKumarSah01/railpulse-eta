package com.railpulse.eta.service;

import com.railpulse.eta.dto.*;
import com.railpulse.eta.entity.*;
import com.railpulse.eta.exception.ResourceNotFoundException;
import com.railpulse.eta.prediction.BaselineEtaPredictor;
import com.railpulse.eta.prediction.MlPredictorAdapter;
import com.railpulse.eta.prediction.PredictionResult;
import com.railpulse.eta.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
public class TrainService {

    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final TrainStopRepository trainStopRepository;
    private final RunningUpdateRepository runningUpdateRepository;
    private final EtaPredictionRepository etaPredictionRepository;
    private final AlertRepository alertRepository;
    private final BaselineEtaPredictor baselineEtaPredictor;
    private final MlPredictorAdapter mlPredictorAdapter;

    public TrainService(
            TrainRepository trainRepository,
            StationRepository stationRepository,
            TrainStopRepository trainStopRepository,
            RunningUpdateRepository runningUpdateRepository,
            EtaPredictionRepository etaPredictionRepository,
            AlertRepository alertRepository,
            BaselineEtaPredictor baselineEtaPredictor,
            MlPredictorAdapter mlPredictorAdapter
    ) {
        this.trainRepository = trainRepository;
        this.stationRepository = stationRepository;
        this.trainStopRepository = trainStopRepository;
        this.runningUpdateRepository = runningUpdateRepository;
        this.etaPredictionRepository = etaPredictionRepository;
        this.alertRepository = alertRepository;
        this.baselineEtaPredictor = baselineEtaPredictor;
        this.mlPredictorAdapter = mlPredictorAdapter;
    }

    public List<TrainSummaryDto> getAllTrains() {
        return trainRepository.findAll().stream()
                .map(train -> new TrainSummaryDto(train.getId(), train.getTrainNumber(), train.getTrainName(), train.isActive()))
                .toList();
    }

    public TrainDetailDto getTrainByNumber(String trainNumber) {
        Train train = trainRepository.findByTrainNumber(trainNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Train not found: " + trainNumber));

        List<RouteStopDto> route = getRoute(trainNumber);
        return new TrainDetailDto(train.getId(), train.getTrainNumber(), train.getTrainName(), train.isActive(), route, "demo", true);
    }

    public List<RouteStopDto> getRoute(String trainNumber) {
        return trainStopRepository.findByTrainNumberOrderByStopSequence(trainNumber).stream()
                .map(stop -> new RouteStopDto(
                        stop.getStopSequence(),
                        stop.getStation().getStationCode(),
                        stop.getStation().getStationName(),
                        stop.getScheduledArrival(),
                        stop.getScheduledDeparture()))
                .toList();
    }

    public EtaPredictionDto getNextEta(String trainNumber) {
        Train train = trainRepository.findByTrainNumber(trainNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Train not found: " + trainNumber));

        List<TrainStop> route = trainStopRepository.findByTrainNumberOrderByStopSequence(trainNumber);
        if (route.isEmpty()) {
            throw new ResourceNotFoundException("No route available for train " + trainNumber);
        }

        Station nextStation = route.get(1).getStation();
        LocalDateTime now = LocalDateTime.now();
        PredictionResult result = baselineEtaPredictor.predict(train, nextStation, now, List.of(train));

        return new EtaPredictionDto(
                null,
                train.getTrainNumber(),
                train.getTrainName(),
                nextStation.getStationCode(),
                nextStation.getStationName(),
                LocalDateTime.of(now.toLocalDate(), route.get(1).getScheduledArrival()),
                result.predictedArrival(),
                result.estimatedDelayMinutes(),
                result.lowerBound(),
                result.upperBound(),
                now,
                result.modelVersion(),
                result.source(),
                result.simulated()
        );
    }

    @Transactional
    public PredictionResponse predictEta(PredictionRequest request) {
        Train train = trainRepository.findByTrainNumber(request.trainNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Train not found: " + request.trainNumber()));

        Station station = stationRepository.findByStationCode(request.stationCode())
                .orElseThrow(() -> new ResourceNotFoundException("Station not found: " + request.stationCode()));

        LocalDateTime now = LocalDateTime.now();
        PredictionResult result = mlPredictorAdapter.predict(train, station, now, List.of(train));

        EtaPrediction prediction = new EtaPrediction();
        prediction.setTrain(train);
        prediction.setStation(station);
        prediction.setPredictedAt(now);
        prediction.setPredictedArrival(result.predictedArrival());
        prediction.setLowerBound(result.lowerBound());
        prediction.setUpperBound(result.upperBound());
        prediction.setModelVersion(result.modelVersion());
        prediction.setInputFreshnessMinutes(12);
        prediction.setPredictionSource(result.source());
        etaPredictionRepository.save(prediction);

        if (result.estimatedDelayMinutes() != null && result.estimatedDelayMinutes() > 10) {
            Alert alert = new Alert(train, station, "DELAY", "Train " + train.getTrainNumber() + " is predicted to arrive " + result.estimatedDelayMinutes() + " minutes late at " + station.getStationName());
            alertRepository.save(alert);
        }

        return new PredictionResponse(
                result.status(),
                result.modelVersion(),
                result.source(),
                result.simulated(),
                train.getTrainNumber(),
                station.getStationCode(),
                result.predictedArrival(),
                result.lowerBound(),
                result.upperBound(),
                result.estimatedDelayMinutes(),
                "Prediction generated using " + result.modelVersion() + "."
        );
    }

    public List<EtaPredictionDto> history(String trainNumber) {
        return etaPredictionRepository.findLatestByTrainNumber(trainNumber).stream()
                .map(p -> new EtaPredictionDto(
                        p.getId(),
                        p.getTrain().getTrainNumber(),
                        p.getTrain().getTrainName(),
                        p.getStation().getStationCode(),
                        p.getStation().getStationName(),
                        p.getPredictedArrival(),
                        p.getPredictedArrival(),
                        p.getInputFreshnessMinutes(),
                        p.getLowerBound(),
                        p.getUpperBound(),
                        p.getPredictedAt(),
                        p.getModelVersion(),
                        p.getPredictionSource(),
                        false
                ))
                .toList();
    }

    public DashboardSummaryDto dashboardSummary() {
        long totalTrains = trainRepository.count();
        long activeTrains = trainRepository.findAll().stream().filter(Train::isActive).count();
        long openAlerts = alertRepository.findAll().stream().filter(alert -> !alert.isAcknowledged()).count();
        String worstDelayTrain = trainRepository.findAll().stream()
                .map(train -> new Object[]{train.getTrainNumber(), 12})
                .findFirst()
                .map(obj -> (String) obj[0])
                .orElse("N/A");

        return new DashboardSummaryDto(totalTrains, activeTrains, openAlerts, worstDelayTrain, "LIVE (DEMO)", 12.0);
    }

    public List<AlertDto> getAlerts() {
        return alertRepository.findAll().stream()
                .sorted(Comparator.comparing(Alert::getCreatedAt).reversed())
                .map(alert -> new AlertDto(
                        alert.getId(),
                        alert.getTrain().getTrainNumber(),
                        alert.getStation() != null ? alert.getStation().getStationCode() : "N/A",
                        alert.getAlertType(),
                        alert.getMessage(),
                        alert.getCreatedAt(),
                        alert.isAcknowledged()))
                .toList();
    }

    public AlertDto acknowledgeAlert(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found: " + id));
        alert.setAcknowledged(true);
        Alert saved = alertRepository.save(alert);
        return new AlertDto(saved.getId(), saved.getTrain().getTrainNumber(), saved.getStation() != null ? saved.getStation().getStationCode() : "N/A", saved.getAlertType(), saved.getMessage(), saved.getCreatedAt(), saved.isAcknowledged());
    }

    public ModelMetricDto getModelMetrics() {
        return new ModelMetricDto("baseline-v1", "baseline", true, 8.4, 10.6, 142, "OK");
    }
}
