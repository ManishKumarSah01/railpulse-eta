package com.railpulse.eta.service;

import com.railpulse.eta.entity.*;
import com.railpulse.eta.repository.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class DemoDataInitializer {

    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final TrainStopRepository trainStopRepository;
    private final RunningUpdateRepository runningUpdateRepository;
    private final AlertRepository alertRepository;

    public DemoDataInitializer(
            TrainRepository trainRepository,
            StationRepository stationRepository,
            TrainStopRepository trainStopRepository,
            RunningUpdateRepository runningUpdateRepository,
            AlertRepository alertRepository
    ) {
        this.trainRepository = trainRepository;
        this.stationRepository = stationRepository;
        this.trainStopRepository = trainStopRepository;
        this.runningUpdateRepository = runningUpdateRepository;
        this.alertRepository = alertRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initialize() {
        if (trainRepository.count() > 0) {
            return;
        }

        Train train = new Train("12951", "Azaad Hind Express");
        train = trainRepository.save(train);

        Station ndls = stationRepository.save(new Station("NDLS", "New Delhi", 28.6139, 77.2090));
        Station agc = stationRepository.save(new Station("AGC", "Agra Cantt", 27.1767, 78.0081));
        Station gzb = stationRepository.save(new Station("GZB", "Ghaziabad", 28.6692, 77.4538));
        Station jp = stationRepository.save(new Station("JP", "Jaipur", 26.9124, 75.7873));

        trainStopRepository.save(new TrainStop(train, ndls, 1, LocalTime.of(8, 0), LocalTime.of(8, 12)));
        trainStopRepository.save(new TrainStop(train, gzb, 2, LocalTime.of(9, 30), LocalTime.of(9, 38)));
        trainStopRepository.save(new TrainStop(train, agc, 3, LocalTime.of(11, 15), LocalTime.of(11, 22)));
        trainStopRepository.save(new TrainStop(train, jp, 4, LocalTime.of(14, 15), LocalTime.of(14, 20)));

        RunningUpdate update = new RunningUpdate();
        update.setTrain(train);
        update.setStation(gzb);
        update.setObservedAt(LocalDateTime.now().minusMinutes(6));
        update.setReceivedAt(LocalDateTime.now());
        update.setCurrentDelayMinutes(15);
        update.setSource("demo");
        update.setSimulated(true);
        runningUpdateRepository.save(update);

        alertRepository.save(new Alert(train, gzb, "DELAY", "Train 12951 is running 15 minutes behind schedule."));
    }
}
