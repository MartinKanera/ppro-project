package org.example.workouttracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "set_logs")
public class SetLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Repetitions must be provided")
    @Min(value = 0, message = "Repetitions must be greater than or equal to 0")
    private Integer repetitions;

    @Column(nullable = false)
    @NotNull(message = "Weight must be provided")
    @Min(value = 0, message = "Weight must be greater than or equal to 0")
    private Double weight;

    @Column(nullable = false)
    private Integer setOrder;

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
