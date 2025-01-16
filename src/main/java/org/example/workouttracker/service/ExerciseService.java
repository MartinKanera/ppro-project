package org.example.workouttracker.service;

import org.example.workouttracker.model.Exercise;
import org.example.workouttracker.model.User;

import java.util.List;

public interface ExerciseService {
    List<Exercise> getSortedExercisesByUser(User user);
    void save(Exercise exercise);
    Exercise getExerciseById(Long id);
    void deleteExercise(Long id);
    List<Exercise> getAllUsedExercisesByUser(User user);
    boolean isExerciseOwner(long exerciseId, long userId);
}
