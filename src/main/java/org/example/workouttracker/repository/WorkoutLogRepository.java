package org.example.workouttracker.repository;

import org.example.workouttracker.model.User;
import org.example.workouttracker.model.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {
    List<WorkoutLog> findAllByUserOrderByLogDateDesc(User user);
    boolean existsByIdAndUser(Long id, User user);
}
