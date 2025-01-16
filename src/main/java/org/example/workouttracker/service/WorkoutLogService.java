package org.example.workouttracker.service;

import org.example.workouttracker.model.User;
import org.example.workouttracker.model.WorkoutLog;

import java.util.List;

public interface WorkoutLogService {
    WorkoutLog findWorkoutLogById(Long workoutLogId);
    List<WorkoutLog> findAllByUserFromNewest(User user);
    void saveWorkoutLog(WorkoutLog workoutLog);
    boolean isWorkoutLogOwner(Long workoutLogId, User user);
}
