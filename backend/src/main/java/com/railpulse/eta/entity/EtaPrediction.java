package com.railpulse.eta.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "eta_predictions")
public class EtaPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(name = "predicted_at", nullable = false)
    private LocalDateTime predictedAt;

    @Column(name = "predicted_arrival", nullable = false)
    private LocalDateTime predictedArrival;

    @Column(name = "lower_bound", nullable = false)
    private LocalDateTime lowerBound;

    @Column(name = "upper_bound", nullable = false)
    private LocalDateTime upperBound;

    @Column(name = "model_version", nullable = false)
    private String modelVersion;

    @Column(name = "input_freshness_minutes", nullable = false)
    private Integer inputFreshnessMinutes;

    @Column(name = "prediction_source", nullable = false)
    private String predictionSource;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Train getTrain() { return train; }
    public void setTrain(Train train) { this.train = train; }
    public Station getStation() { return station; }
    public void setStation(Station station) { this.station = station; }
    public LocalDateTime getPredictedAt() { return predictedAt; }
    public void setPredictedAt(LocalDateTime predictedAt) { this.predictedAt = predictedAt; }
    public LocalDateTime getPredictedArrival() { return predictedArrival; }
    public void setPredictedArrival(LocalDateTime predictedArrival) { this.predictedArrival = predictedArrival; }
    public LocalDateTime getLowerBound() { return lowerBound; }
    public void setLowerBound(LocalDateTime lowerBound) { this.lowerBound = lowerBound; }
    public LocalDateTime getUpperBound() { return upperBound; }
    public void setUpperBound(LocalDateTime upperBound) { this.upperBound = upperBound; }
    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }
    public Integer getInputFreshnessMinutes() { return inputFreshnessMinutes; }
    public void setInputFreshnessMinutes(Integer inputFreshnessMinutes) { this.inputFreshnessMinutes = inputFreshnessMinutes; }
    public String getPredictionSource() { return predictionSource; }
    public void setPredictionSource(String predictionSource) { this.predictionSource = predictionSource; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
