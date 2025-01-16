package org.example.workouttracker.repository;

import org.example.workouttracker.model.Exercise;
import org.example.workouttracker.model.ExerciseWorkout;
import org.example.workouttracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> getExercisesByUserOrderByName(User user);
    boolean existsByIdAndUserId(long exerciseId, long userId);
    @Query("SELECT e FROM Exercise e WHERE e.id IN (SELECT ew.exercise.id FROM ExerciseWorkout ew WHERE ew.workout.user = ?1)")
    List<Exercise> getAllUsedExercisesByUser(User user);
}
