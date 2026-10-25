package com.railpulse.eta.repository;

import com.railpulse.eta.entity.EtaPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EtaPredictionRepository extends JpaRepository<EtaPrediction, Long> {
    @Query("SELECT e FROM EtaPrediction e JOIN FETCH e.train t WHERE t.trainNumber = :trainNumber ORDER BY e.predictedAt DESC")
    List<EtaPrediction> findLatestByTrainNumber(@Param("trainNumber") String trainNumber);
}
