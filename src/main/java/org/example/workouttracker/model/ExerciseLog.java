package org.example.workouttracker.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "exercise_logs")
public class ExerciseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String exerciseName;
    private Integer exerciseOrder;

    @Enumerated(EnumType.STRING)
    private Exercise.MuscleGroup muscleGroup;

    @ManyToOne
    @JoinColumn(name = "workout_log_id", nullable = false)
    private WorkoutLog workoutLog;

    @OneToMany(mappedBy = "exerciseLog", cascade = CascadeType.ALL)
    private List<SetLog> setLogs;


    public ExerciseLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public Integer getExerciseOrder() {
        return exerciseOrder;
    }

    public void setExerciseOrder(Integer exerciseOrder) {
        this.exerciseOrder = exerciseOrder;
    }

    public Exercise.MuscleGroup getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(Exercise.MuscleGroup muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public WorkoutLog getWorkoutLog() {
        return workoutLog;
    }

    public void setWorkoutLog(WorkoutLog workoutLog) {
        this.workoutLog = workoutLog;
    }

    public List<SetLog> getSetLogs() {
        return setLogs;
    }

    public void setSetLogs(List<SetLog> setLogs) {
        this.setLogs = setLogs;
    }
}
