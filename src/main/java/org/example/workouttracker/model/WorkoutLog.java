package org.example.workouttracker.model;

import jakarta.persistence.*;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "workout_logs")
public class WorkoutLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date logDate = new Date(); // Date of the workout

    private String workoutName;

    @OneToMany(mappedBy = "workoutLog", cascade = CascadeType.ALL)
    private List<ExerciseLog> exerciseLogs = new ArrayList<>();

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

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
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

    public String getFormattedDate() {
        Locale locale = new Locale("cs", "CZ");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        return dateFormat.format(this.logDate);
    }

    public void setUser(User user) {
        this.user = user;
    }

        public static WorkoutLog initFromWorkout(Workout workout) {
        WorkoutLog workoutLog = new WorkoutLog();
        workoutLog.setWorkoutName(workout.getName());

        List<ExerciseWorkout> exerciseWorkouts = workout.getExerciseWorkouts();
        for (ExerciseWorkout exerciseWorkout : exerciseWorkouts) {
            ExerciseLog exerciseLog = new ExerciseLog();
            exerciseLog.setExerciseName(exerciseWorkout.getExercise().getName());
            exerciseLog.setExerciseOrder(exerciseWorkout.getIndex());
            exerciseLog.setMuscleGroup(exerciseWorkout.getExercise().getMuscleGroup());
            exerciseLog.setWorkoutLog(workoutLog);

            int numberOfSets = exerciseWorkout.getSets();

            for (int i = 0; i < numberOfSets; i++) {
                SetLog setLog = new SetLog();
                setLog.setSetOrder(i);
                setLog.setExerciseLog(exerciseLog);
                exerciseLog.getSetLogs().add(setLog);
            }

            workoutLog.getExerciseLogs().add(exerciseLog);
        }

        return workoutLog;
    }
}
