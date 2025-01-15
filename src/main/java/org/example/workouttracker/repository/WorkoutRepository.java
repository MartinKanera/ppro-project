package org.example.workouttracker.repository;

import org.example.workouttracker.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long>  {
    List<Workout> getAllByUserId(Long userId);
    boolean existsByIdAndUserId(Long workoutId, Long userId);
}
