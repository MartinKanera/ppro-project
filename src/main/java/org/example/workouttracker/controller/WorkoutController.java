package org.example.workouttracker.controller;

import jakarta.validation.Valid;
import org.example.workouttracker.model.Workout;
import org.example.workouttracker.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.example.workouttracker.service.WorkoutService;

@Controller
public class WorkoutController {

    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping("/workouts")
    public String list(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.addAttribute("workouts", workoutService.getAllWorkoutsForUser(customUserDetails.getUserId()));

        return "workout/list";
    }

    @PostMapping("/workout/save")
    public String save(@Valid Workout workout, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        workout.setUser(customUserDetails.getUser());
        workoutService.save(workout);

        return "redirect:/workouts";
    }

    @GetMapping("/workout/create")
    public String crate(Model model) {
        model.addAttribute("workout", new Workout());
        model.addAttribute("edit", false);

        return "workout/edit";
    }
}
