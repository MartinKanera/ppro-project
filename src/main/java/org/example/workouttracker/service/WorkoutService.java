package org.example.workouttracker.service;

import org.example.workouttracker.model.Workout;

import java.util.List;

public interface WorkoutService {
    void save(Workout workout);
    List<Workout> getAllWorkoutsForUser(long userId);
    Workout getWorkoutById(long workoutId);
    void deleteWorkout(long id);
    boolean isWorkoutOwner(long workoutId, long userId);
}
