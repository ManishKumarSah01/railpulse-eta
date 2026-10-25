package com.railpulse.eta.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "train_stops")
public class TrainStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(name = "stop_sequence", nullable = false)
    private Integer stopSequence;

    @Column(name = "scheduled_arrival")
    private LocalTime scheduledArrival;

    @Column(name = "scheduled_departure")
    private LocalTime scheduledDeparture;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public TrainStop() {
    }

    public TrainStop(Train train, Station station, Integer stopSequence, LocalTime scheduledArrival, LocalTime scheduledDeparture) {
        this.train = train;
        this.station = station;
        this.stopSequence = stopSequence;
        this.scheduledArrival = scheduledArrival;
        this.scheduledDeparture = scheduledDeparture;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Train getTrain() { return train; }
    public void setTrain(Train train) { this.train = train; }
    public Station getStation() { return station; }
    public void setStation(Station station) { this.station = station; }
    public Integer getStopSequence() { return stopSequence; }
    public void setStopSequence(Integer stopSequence) { this.stopSequence = stopSequence; }
    public LocalTime getScheduledArrival() { return scheduledArrival; }
    public void setScheduledArrival(LocalTime scheduledArrival) { this.scheduledArrival = scheduledArrival; }
    public LocalTime getScheduledDeparture() { return scheduledDeparture; }
    public void setScheduledDeparture(LocalTime scheduledDeparture) { this.scheduledDeparture = scheduledDeparture; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
