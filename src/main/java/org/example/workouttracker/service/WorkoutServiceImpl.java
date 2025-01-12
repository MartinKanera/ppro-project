package org.example.workouttracker.service;

import org.example.workouttracker.model.Workout;
import org.example.workouttracker.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;

    public WorkoutServiceImpl(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @Override
    public void save(Workout workout) {
        workoutRepository.save(workout);
    }

    @Override
    public List<Workout> getAllWorkoutsForUser(long userId) {
        return workoutRepository.getAllByUserId(userId);
    }

    @Override
    public Workout getWorkoutById(long workoutId) {
        return workoutRepository.findById(workoutId).
                orElseThrow(() -> new IllegalArgumentException("Invalid workout ID"));
    }
}
