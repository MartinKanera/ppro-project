package org.example.workouttracker.controller;

import jakarta.validation.Valid;
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

        boolean isNew = workout.getId() == null;
        boolean isOwner = !isNew && workoutService.isWorkoutOwner(workout.getId(), customUserDetails.getUserId());

        if (!isNew && !isOwner) {
            return "redirect:/workouts";
        }

        workout.getExerciseWorkouts()
                .forEach(exerciseWorkout -> exerciseWorkout.setWorkout(workout));

        workoutService.save(workout);

        return "redirect:/workouts";
    }

    @PostMapping("/workout/addExercise")
    public String addExercise(@ModelAttribute Workout workout, @RequestParam("edit") boolean edit, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        boolean isNew = workout.getId() == null;
        boolean isOwner = !isNew && workoutService.isWorkoutOwner(workout.getId(), customUserDetails.getUserId());

        if (!isNew && !isOwner) {
            return "redirect:/workouts";
        }

        workout.addEmptyExerciseWorkout();

        redirectAttributes.addFlashAttribute("workout", workout);

        if (edit) {
            return "redirect:/workout/edit/" + workout.getId();
        } else {
            return "redirect:/workout/create";
        }
    }

    @PostMapping("/workout/removeExercise/{index}")
    public String removeExercise(@ModelAttribute Workout workout, @PathVariable int index, @RequestParam("edit") boolean edit, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        boolean isNew = workout.getId() == null;
        boolean isOwner = !isNew && workoutService.isWorkoutOwner(workout.getId(), customUserDetails.getUserId());

        if (!isNew && !isOwner) {
            return "redirect:/workouts";
        }

        workout.removeExerciseWorkout(index);

        redirectAttributes.addFlashAttribute("workout", workout);

        if (edit) {
            return "redirect:/workout/edit/" + workout.getId();
        } else {
            return "redirect:/workout/create";
        }
    }

    @GetMapping("/workout/create")
    public String create(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (!model.containsAttribute("workout")) {
            Workout workout = new Workout();
            workout.setUser(customUserDetails.getUser());
            workout.addEmptyExerciseWorkout();
            model.addAttribute("workout", workout);
        }
        model.addAttribute("edit", false);
        model.addAttribute("existingExercises", exerciseService.getAllExercises());
        return "workout/edit";
    }

    @GetMapping("/workout/edit/{id}")
    public String edit(Model model, @PathVariable long id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        if (!workoutService.isWorkoutOwner(id, customUserDetails.getUserId())) {
            return "redirect:/workouts";
        }

        if (!model.containsAttribute("workout")) {
            Workout workout = workoutService.getWorkoutById(id);
            model.addAttribute("workout", workout);
        }
        model.addAttribute("edit", true);
        model.addAttribute("existingExercises", exerciseService.getAllExercises());
        return "workout/edit";
    }

    @GetMapping("/workout/delete/{id}")
    public String delete(@PathVariable long id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        if (workoutService.isWorkoutOwner(id, customUserDetails.getUserId())) {
            workoutService.deleteWorkout(id);
        }

        return "redirect:/workouts";
    }
}
