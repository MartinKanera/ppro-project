package org.example.workouttracker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "set_logs")
public class SetLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer repetitions; // Number of repetitions
    private Double weight; // Weight lifted for this set
    private Integer setOrder; // Order of the set in the workout

    @ManyToOne
    @JoinColumn(name = "exercise_log_id", nullable = false)
    private ExerciseLog exerciseLog;

    public SetLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getSetOrder() {
        return setOrder;
    }

    public void setSetOrder(Integer setOrder) {
        this.setOrder = setOrder;
    }

    public ExerciseLog getExerciseLog() {
        return exerciseLog;
    }

    public void setExerciseLog(ExerciseLog exerciseLog) {
        this.exerciseLog = exerciseLog;
    }
}
