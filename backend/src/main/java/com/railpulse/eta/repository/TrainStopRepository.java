package com.railpulse.eta.repository;

import com.railpulse.eta.entity.TrainStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrainStopRepository extends JpaRepository<TrainStop, Long> {
    @Query("SELECT ts FROM TrainStop ts JOIN FETCH ts.station s JOIN FETCH ts.train t WHERE t.trainNumber = :trainNumber ORDER BY ts.stopSequence ASC")
    List<TrainStop> findByTrainNumberOrderByStopSequence(@Param("trainNumber") String trainNumber);
}
