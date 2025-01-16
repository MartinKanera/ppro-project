package org.example.workouttracker.service;

import org.example.workouttracker.model.User;
import org.example.workouttracker.model.WorkoutLog;
import org.example.workouttracker.repository.WorkoutLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutLogServiceImpl implements WorkoutLogService {

    private final WorkoutLogRepository workoutLogRepository;

    public WorkoutLogServiceImpl(WorkoutLogRepository workoutLogRepository) {
        this.workoutLogRepository = workoutLogRepository;
    }

    @Override
    public WorkoutLog findWorkoutLogById(Long workoutLogId) {
        return workoutLogRepository.findById(workoutLogId).orElse(null);
    }

    @Override
    public List<WorkoutLog> findAllByUserFromNewest(User user) {
        return workoutLogRepository.findAllByUserOrderByLogDateDesc(user);
    }

    @Override
    public void saveWorkoutLog(WorkoutLog workoutLog) {
        workoutLogRepository.save(workoutLog);
    }

    @Override
    public boolean isWorkoutLogOwner(Long workoutLogId, User user) {
        return workoutLogRepository.existsByIdAndUser(workoutLogId, user);
    }
}
