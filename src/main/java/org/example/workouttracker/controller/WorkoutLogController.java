package org.example.workouttracker.controller;

import jakarta.validation.Valid;
import org.example.workouttracker.model.WorkoutLog;
import org.example.workouttracker.security.CustomUserDetails;
import org.example.workouttracker.service.WorkoutLogService;
import org.example.workouttracker.service.WorkoutService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WorkoutLogController {

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

        if (model.containsAttribute("workoutLog")) {
            return "workout-log/create";
        }

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

    @PostMapping("/workoutLog/save/{workoutId}")
    public String logWorkout(
            @Valid @ModelAttribute WorkoutLog workoutLog,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable long workoutId,
            RedirectAttributes redirectAttributes
        ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.workoutLog", bindingResult);
            redirectAttributes.addFlashAttribute("workoutLog", workoutLog);
            return "redirect:/workoutLog/create/" + workoutId;
        }

        workoutLog.setUser(customUserDetails.getUser());

        workoutLog.getExerciseLogs().forEach(exerciseLog -> {
            exerciseLog.setWorkoutLog(workoutLog);
            exerciseLog.getSetLogs().forEach(setLog -> setLog.setExerciseLog(exerciseLog));
        });

        workoutLogService.saveWorkoutLog(workoutLog);
        return "redirect:/";
    }
}
