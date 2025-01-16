package org.example.workouttracker.controller;

import org.example.workouttracker.model.WorkoutLog;
import org.example.workouttracker.security.CustomUserDetails;
import org.example.workouttracker.service.WorkoutLogService;
import org.example.workouttracker.service.WorkoutService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WorkoutLogController {

    public class WorkoutLogInfo {
        private final long id;
        private final String formattedDate;
        private final String workoutName;

        public WorkoutLogInfo(long id, String formattedDate, String workoutName) {
            this.id = id;
            this.formattedDate = formattedDate;
            this.workoutName = workoutName;
        }

        public long getId() {
            return id;
        }

        public String getFormattedDate() {
            return formattedDate;
        }

        public String getWorkoutName() {
            return workoutName;
        }
    }

    private final WorkoutLogService workoutLogService;
    private final WorkoutService workoutService;

    public WorkoutLogController(WorkoutLogService workoutLogService, WorkoutService workoutService) {
        this.workoutLogService = workoutLogService;
        this.workoutService = workoutService;
    }

    @GetMapping("/")
    public String listWorkoutLogs(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        model.addAttribute("username", customUserDetails.getUser().getUsername());
        model.addAttribute("workoutLogs", workoutLogService.findAllByUserFromNewest(customUserDetails.getUser()));
        model.addAttribute("workouts", workoutService.getAllWorkoutsForUser(customUserDetails.getUser().getId()));

        return "index";
    }

    @GetMapping("/workoutLog/create/{workoutId}")
    public String createWorkoutLog(@PathVariable long workoutId, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {

        if (!workoutService.isWorkoutOwner(workoutId, customUserDetails.getUser().getId())) return "redirect:/";

        WorkoutLog workoutLog = WorkoutLog.initFromWorkout(workoutService.getWorkoutById(workoutId));

        model.addAttribute("workoutLog", workoutLog);

        return "workout-log/create";
    }

    @GetMapping("/workoutLog/{workoutLogId}")
    public String viewWorkoutLog(@PathVariable long workoutLogId, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {

        if (!workoutLogService.isWorkoutLogOwner(workoutLogId, customUserDetails.getUser())) return "redirect:/";

        WorkoutLog workoutLog = workoutLogService.findWorkoutLogById(workoutLogId);

        model.addAttribute("workoutLog", workoutLog);

        return "workout-log/view";
    }

    @PostMapping("/workoutLog/save")
    public String logWorkout(@ModelAttribute WorkoutLog workoutLog, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        workoutLog.setUser(customUserDetails.getUser());

        workoutLog.getExerciseLogs().forEach(exerciseLog -> {
            exerciseLog.setWorkoutLog(workoutLog);
            exerciseLog.getSetLogs().forEach(setLog -> setLog.setExerciseLog(exerciseLog));
        });

        workoutLogService.saveWorkoutLog(workoutLog);
        return "redirect:/";
    }
}
