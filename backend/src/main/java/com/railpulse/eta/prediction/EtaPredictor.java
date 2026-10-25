package com.railpulse.eta.prediction;

import com.railpulse.eta.entity.Station;
import com.railpulse.eta.entity.Train;

import java.time.LocalDateTime;
import java.util.List;

public interface EtaPredictor {
    PredictionResult predict(Train train, Station station, LocalDateTime now, List<Train> trains);
}
