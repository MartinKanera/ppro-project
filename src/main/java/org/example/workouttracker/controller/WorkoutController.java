package org.example.workouttracker.controller;

import jakarta.validation.Valid;
import org.example.workouttracker.model.Exercise;
import org.example.workouttracker.model.Workout;
import org.example.workouttracker.security.CustomUserDetails;
import org.example.workouttracker.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.workouttracker.service.WorkoutService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class WorkoutController {

    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;

    @Autowired
    public WorkoutController(WorkoutService workoutService, ExerciseService exerciseService) {
        this.workoutService = workoutService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/workouts")
    public String list(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.addAttribute("workouts", workoutService.getAllWorkoutsForUser(customUserDetails.getUserId()));

        return "workout/list";
    }

    @PostMapping("/workout/save")
    public String save(@Valid Workout workout, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        workout.setUser(customUserDetails.getUser());
        workout.getExerciseWorkouts().forEach(exerciseWorkout -> exerciseWorkout.setWorkout(workout));
        workoutService.save(workout);

        return "redirect:/workouts";
    }

    @PostMapping("/workout/addExercise")
    public String addExercise(@ModelAttribute Workout workout, @RequestParam("edit") boolean edit, RedirectAttributes redirectAttributes) {
        workout.addEmptyExerciseWorkout();

        redirectAttributes.addFlashAttribute("workout", workout);
        redirectAttributes.addFlashAttribute("existingExercises", exerciseService.getAllExercises());
        redirectAttributes.addFlashAttribute("edit", edit);

        if (edit) {
            return "redirect:/workout/edit/" + workout.getId();
        } else {
            return "redirect:/workout/create";
        }
    }

    @GetMapping("/workout/create")
    public String create(Model model) {
        if (!model.containsAttribute("workout")) {
            Workout workout = new Workout();
            workout.addEmptyExerciseWorkout();
            model.addAttribute("workout", workout);
        }
        model.addAttribute("edit", false);
        model.addAttribute("existingExercises", exerciseService.getAllExercises());
        return "workout/edit";
    }

    @GetMapping("/workout/edit/{id}")
    public String edit(Model model, @PathVariable long id) {

        if (!model.containsAttribute("workout")) {
            Workout workout = workoutService.getWorkoutById(id);
            model.addAttribute("workout", workout);
        }
        model.addAttribute("edit", true);
        model.addAttribute("existingExercises", exerciseService.getAllExercises());
        return "workout/edit";
    }
}
