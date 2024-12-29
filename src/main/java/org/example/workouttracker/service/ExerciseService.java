package org.example.workouttracker.service;

import org.example.workouttracker.model.Exercise;

import java.util.List;

public interface ExerciseService {
    List<Exercise> getAllExercises();
    void save(Exercise exercise);
    Exercise getExerciseById(Long id);
}
