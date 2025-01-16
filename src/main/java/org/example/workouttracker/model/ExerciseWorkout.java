package org.example.workouttracker.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "exercise_workouts")
public class ExerciseWorkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @NotNull(message = "Exercise must be selected")
    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(nullable = false)
    @NotNull(message = "Sets must be provided")
    @Min(value = 1, message = "Sets must be greater than 0")
    private Integer sets;

    @Column(nullable = false)
    @NotNull(message = "Index must be provided")
    @Max(value = 99, message = "Index must be between 0 and 99")
    private Integer index = 0;

    // Constructors
    public ExerciseWorkout() {
    }

    public ExerciseWorkout(Workout workout, Exercise exercise) {
        this.workout = workout;
        this.exercise = exercise;
    }

    // Getters and Setters
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

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}


