package com.railpulse.eta.prediction;

import com.railpulse.eta.entity.Station;
import com.railpulse.eta.entity.Train;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class MlPredictorAdapter implements EtaPredictor {

    @Override
    public PredictionResult predict(Train train, Station station, LocalDateTime now, List<Train> trains) {
        Path modelPath = Path.of(System.getProperty("user.dir"), "ml", "model", "eta_model.onnx");
        if (!Files.exists(modelPath)) {
            return new PredictionResult(
                    train.getTrainNumber(),
                    station.getStationCode(),
                    "fallback-baseline",
                    "demo",
                    true,
                    now.plusMinutes(25),
                    now.plusMinutes(18),
                    now.plusMinutes(35),
                    12,
                    "fallback"
            );
        }

        return new PredictionResult(
                train.getTrainNumber(),
                station.getStationCode(),
                "ml-v1",
                "demo",
                false,
                now.plusMinutes(20),
                now.plusMinutes(15),
                now.plusMinutes(28),
                8,
                "ml"
        );
    }
}
