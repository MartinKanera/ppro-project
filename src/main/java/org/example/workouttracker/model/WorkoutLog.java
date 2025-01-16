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

    private LocalDateTime logDate = LocalDateTime.now(); // Date of the workout

    private String workoutName;

    @OneToMany(mappedBy = "workoutLog", cascade = CascadeType.ALL)
    private List<ExerciseLog> exerciseLogs;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public WorkoutLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDateTime logDate) {
        this.logDate = logDate;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public List<ExerciseLog> getExerciseLogs() {
        return exerciseLogs;
    }

    public void setExerciseLogs(List<ExerciseLog> exerciseLogs) {
        this.exerciseLogs = exerciseLogs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //    public static WorkoutLog initFromWorkout(Workout workout) {
//        WorkoutLog workoutLog = new WorkoutLog();
//        workoutLog.setWorkoutName(workout.getName());
//
//        int setLogIndex = 0;
//
//        List<ExerciseWorkout> exerciseWorkouts = workout.getExerciseWorkouts();
//        for (ExerciseWorkout exerciseWorkout : exerciseWorkouts) {
//            int numberOfSets = exerciseWorkout.getSets();
//
//            for (int setN = 0; setN < numberOfSets; setN++) {
//                SetLog setLog = new SetLog();
//                setLog.setExerciseName(exerciseWorkout.getExercise().getName());
//                setLog.setMuscleGroup(exerciseWorkout.getExercise().getMuscleGroup());
////                setLog.setRepetitions(0);
////                setLog.setWeight(0.0);
//                setLog.setIndex(setLogIndex++);
//                setLog.setWorkoutLog(workoutLog);
//                workoutLog.getSetLogs().add(setLog);
//            }
//        }
//
//        return workoutLog;
//    }
}
