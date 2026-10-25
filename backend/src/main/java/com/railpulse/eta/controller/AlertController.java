package com.railpulse.eta.controller;

import com.railpulse.eta.dto.AlertDto;
import com.railpulse.eta.service.TrainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AlertController {

    private final TrainService trainService;

    public AlertController(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping("/alerts")
    public List<AlertDto> alerts() {
        return trainService.getAlerts();
    }

    @PostMapping("/alerts/{id}/acknowledge")
    public AlertDto acknowledge(@PathVariable Long id) {
        return trainService.acknowledgeAlert(id);
    }
}
