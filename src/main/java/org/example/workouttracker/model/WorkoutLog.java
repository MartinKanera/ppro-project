package org.example.workouttracker.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "workout_logs")
public class WorkoutLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout; // The template this log is based on

    @OneToMany(mappedBy = "workoutLog", cascade = CascadeType.ALL)
    private List<SetLog> setLogs;

    private LocalDateTime logDate = LocalDateTime.now(); // Date of the workout

    // Constructors

    public WorkoutLog() {
    }

    public WorkoutLog(Workout workout) {
        this.workout = workout;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public List<SetLog> getSetLogs() {
        return setLogs;
    }

    public void setSetLogs(List<SetLog> setLogs) {
        this.setLogs = setLogs;
    }

    public LocalDateTime getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDateTime logDate) {
        this.logDate = logDate;
    }
}
