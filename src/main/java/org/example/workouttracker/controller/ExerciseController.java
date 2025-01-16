package org.example.workouttracker.controller;

import jakarta.validation.Valid;
import org.example.workouttracker.model.Exercise;
import org.example.workouttracker.model.ExerciseWorkout;
import org.example.workouttracker.model.Workout;
import org.example.workouttracker.security.CustomUserDetails;
import org.example.workouttracker.service.ExerciseService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ExerciseController {

    public class ExerciseForm {
        @Valid
        private final Exercise exercise;
        private final Workout workout;
        private final boolean isRedirectToWorkout;

        public ExerciseForm(Exercise exercise, Workout workout, boolean isRedirectToWorkout) {
            this.exercise = exercise;
            this.workout = workout;
            this.isRedirectToWorkout = isRedirectToWorkout;
        }

        public Exercise getExercise() {
            return exercise;
        }

        public Workout getWorkout() {
            return workout;
        }

        public boolean getIsRedirectToWorkout() {
            return isRedirectToWorkout;
        }
    }

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/exercises")
    public String list(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<Exercise> exercises = exerciseService.getSortedExercisesByUser(customUserDetails.getUser());
        model.addAttribute("exercises", exercises);
        model.addAttribute("usedExercises", exerciseService.getAllUsedExercisesByUser(customUserDetails.getUser()));

        return "exercise/list";
    }

    @PostMapping("/exercise/create")
    public String createWithFlashAttributes(@ModelAttribute Workout workout, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("workout", workout);

        return "redirect:/exercise/create?redirectToWorkout=true";
    }

    @GetMapping("/exercise/create")
    public String create(@ModelAttribute Workout workout, @RequestParam("redirectToWorkout") Optional<Boolean> redirectToWorkout, Model model) {

        if (!model.containsAttribute("exerciseForm")) {
            model.addAttribute("exerciseForm", new ExerciseForm(
                    new Exercise(),
                    workout,
                    redirectToWorkout.orElse(false)
            ));
        }

        Map<Exercise.MuscleGroup, String> muscleGroups = Exercise.MuscleGroup.getMuscleGroupMap();
        model.addAttribute("muscleGroups", muscleGroups);
        model.addAttribute("edit", false);

        return "exercise/edit";
    }

    @GetMapping("/exercise/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        if (!exerciseService.isExerciseOwner(id, customUserDetails.getUserId())) {
            return "redirect:/workouts";
        }

        Map<Exercise.MuscleGroup, String> muscleGroups = Exercise.MuscleGroup.getMuscleGroupMap();

        if (!model.containsAttribute("exerciseForm")) {
            Exercise exercise = exerciseService.getExerciseById(id);
            model.addAttribute("exerciseForm", new ExerciseForm(
                    exercise,
                    new Workout(),
                    false
            ));
        }

        model.addAttribute("muscleGroups", muscleGroups);
        model.addAttribute("edit", true);

        return "exercise/edit";
    }

    @GetMapping("/exercise/delete/{id}")
    public String delete(@PathVariable("id") Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        if (exerciseService.isExerciseOwner(id, customUserDetails.getUserId())) {
            exerciseService.deleteExercise(id);
        }

        return "redirect:/exercises";
    }

    @PostMapping("/exercise/save")
    public String save(
            @Valid @ModelAttribute ExerciseForm exerciseForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("exerciseForm", exerciseForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.exerciseForm", bindingResult);

            boolean isEdit = exerciseForm.exercise.getId() != null;

            if (isEdit) {
                return "redirect:/exercise/edit/" + exerciseForm.exercise.getId();
            } else {
                return "redirect:/exercise/create";
            }
        }

        boolean isNew = exerciseForm.exercise.getId() == null;
        boolean isOwner = !isNew && exerciseService.isExerciseOwner(exerciseForm.exercise.getId(), customUserDetails.getUserId());

        if (!isNew && !isOwner) {
            return "redirect:/exercises";
        }

        if (exerciseForm.workout != null) {

            int lastExerciseIndex = exerciseForm.workout.getExerciseWorkouts().size() - 1;

            boolean lastExerciseIsEmpty = !exerciseForm.workout.getExerciseWorkouts().isEmpty()
                    && exerciseForm.workout.getExerciseWorkouts().get(lastExerciseIndex).getExercise().getId() == null;

            if (lastExerciseIsEmpty) {
                // Remove last exercise if it was empty, which will be replaced by the new exercise
                exerciseForm.workout.getExerciseWorkouts().remove(lastExerciseIndex);
            }

            List<ExerciseWorkout> exerciseWorkouts = exerciseForm.workout.getExerciseWorkouts();
            ExerciseWorkout exerciseWorkout = new ExerciseWorkout(exerciseForm.workout, exerciseForm.exercise);
            exerciseWorkout.setIndex(exerciseWorkouts.size());
            exerciseWorkouts.add(exerciseWorkout);
            exerciseForm.workout.setExerciseWorkouts(exerciseWorkouts);
        }

        exerciseForm.exercise.setUser(customUserDetails.getUser());

        redirectAttributes.addFlashAttribute("workout", exerciseForm.workout);
        exerciseService.save(exerciseForm.exercise);

        if (exerciseForm.getIsRedirectToWorkout()) {

            Workout workout = exerciseForm.getWorkout();

            if (workout.getId() != null) {
                // Workout was being edited
                return "redirect:/workout/edit/" + workout.getId();
            } else {
                // Workout was being created
                return "redirect:/workout/create";
            }
        }

        return "redirect:/exercises";
    }
}
