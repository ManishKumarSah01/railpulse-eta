package com.railpulse.eta.prediction;

import com.railpulse.eta.entity.Station;
import com.railpulse.eta.entity.Train;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class BaselineEtaPredictor implements EtaPredictor {

    @Override
    public PredictionResult predict(Train train, Station station, LocalDateTime now, List<Train> trains) {
        int currentDelayMinutes = 0;
        LocalDateTime scheduledArrival = now.plusHours(2);

        LocalTime scheduledTime = LocalTime.of(9, 25);
        if (scheduledTime != null) {
            scheduledArrival = LocalDateTime.of(now.toLocalDate(), scheduledTime);
            if (scheduledArrival.isBefore(now)) {
                scheduledArrival = scheduledArrival.plusDays(1);
            }
        }

        int estimatedDelay = Math.max(0, currentDelayMinutes);
        LocalDateTime predicted = scheduledArrival.plusMinutes(estimatedDelay);
        LocalDateTime lower = predicted.minusMinutes(8);
        LocalDateTime upper = predicted.plusMinutes(12);

        return new PredictionResult(
                train.getTrainNumber(),
                station.getStationCode(),
                "baseline-v1",
                "demo",
                false,
                predicted,
                lower,
                upper,
                estimatedDelay,
                "baseline"
        );
    }
}
