package com.railpulse.eta.repository;

import com.railpulse.eta.entity.RunningUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RunningUpdateRepository extends JpaRepository<RunningUpdate, Long> {
    List<RunningUpdate> findByTrain_TrainNumberOrderByObservedAtDesc(String trainNumber);
}
