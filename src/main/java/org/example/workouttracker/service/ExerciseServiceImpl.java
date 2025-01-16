package org.example.workouttracker.service;

import org.example.workouttracker.model.Exercise;
import org.example.workouttracker.model.User;
import org.example.workouttracker.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public List<Exercise> getSortedExercisesByUser(User user) {
        return exerciseRepository.getExercisesByUserOrderByName(user);
    }

    @Override
    public void save(Exercise exercise) {
        exerciseRepository.save(exercise);
    }

    @Override
    public Exercise getExerciseById(Long id) {
        return exerciseRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteExercise(Long id) {
        exerciseRepository.deleteById(id);
    }

    @Override
    public List<Exercise> getAllUsedExercisesByUser(User user) {
        return exerciseRepository.getAllUsedExercisesByUser(user);
    }

    @Override
    public boolean isExerciseOwner(long exerciseId, long userId) {
        return exerciseRepository.existsByIdAndUserId(exerciseId, userId);
    }
}
