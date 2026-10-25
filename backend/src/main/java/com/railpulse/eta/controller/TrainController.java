package com.railpulse.eta.controller;

import com.railpulse.eta.dto.*;
import com.railpulse.eta.service.TrainService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TrainController {

    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping("/health")
    public ResponseEntity<ApiHealthDto> health() {
        return ResponseEntity.ok(new ApiHealthDto("UP", "demo", java.time.LocalDateTime.now(), "RailPulse ETA is operational."));
    }

    @GetMapping("/trains")
    public List<TrainSummaryDto> allTrains() {
        return trainService.getAllTrains();
    }

    @GetMapping("/trains/{trainNumber}")
    public TrainDetailDto train(@PathVariable String trainNumber) {
        return trainService.getTrainByNumber(trainNumber);
    }

    @GetMapping("/trains/{trainNumber}/route")
    public List<RouteStopDto> route(@PathVariable String trainNumber) {
        return trainService.getRoute(trainNumber);
    }

    @GetMapping("/trains/{trainNumber}/eta")
    public EtaPredictionDto eta(@PathVariable String trainNumber) {
        return trainService.getNextEta(trainNumber);
    }

    @PostMapping("/eta/predict")
    public PredictionResponse predict(@Valid @RequestBody PredictionRequest request) {
        return trainService.predictEta(request);
    }

    @GetMapping("/eta/history/{trainNumber}")
    public List<EtaPredictionDto> history(@PathVariable String trainNumber) {
        return trainService.history(trainNumber);
    }

    @GetMapping("/dashboard/summary")
    public DashboardSummaryDto summary() {
        return trainService.dashboardSummary();
    }

    @GetMapping("/metrics/model")
    public ModelMetricDto metrics() {
        return trainService.getModelMetrics();
    }
}
