package org.example.workouttracker.controller;

import org.example.workouttracker.model.Exercise;
import org.example.workouttracker.model.ExerciseWorkout;
import org.example.workouttracker.model.Workout;
import org.example.workouttracker.security.CustomUserDetails;
import org.example.workouttracker.service.ExerciseService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class ExerciseController {

    public class ExerciseForm {
        private final Exercise exercise;
        private final Workout workout;
        private final boolean editWorkout;

        public ExerciseForm(Exercise exercise, Workout workout, boolean editWorkout) {
            this.exercise = exercise;
            this.workout = workout;
            this.editWorkout = editWorkout;
        }

        public Exercise getExercise() {
            return exercise;
        }

        public Workout getWorkout() {
            return workout;
        }

        public boolean getEditWorkout() {
            return editWorkout;
        }
    }

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping("/exercise/create")
    public String createWithFlashAttributes(@ModelAttribute Workout workout, @RequestParam("editWorkout") boolean edit, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("workout", workout);

        return "redirect:/exercise/create?editWorkout=" + edit;
    }

    @GetMapping("/exercise/create")
    public String create(@ModelAttribute Workout workout, @RequestParam("editWorkout") boolean editWorkout, Model model) {

        Map<Exercise.MuscleGroup, String> muscleGroups = Exercise.MuscleGroup.getMuscleGroupMap();

        model.addAttribute("muscleGroups", muscleGroups);
        model.addAttribute("exerciseForm", new ExerciseForm(new Exercise(), workout, editWorkout));
        // Is exercise edited?
        model.addAttribute("edit", false);

        return "exercise/edit";
    }

    @PostMapping("/exercise/save")
    public String save(ExerciseForm exerciseForm, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        if (exerciseForm.workout != null) {
            List<ExerciseWorkout> exerciseWorkouts = exerciseForm.workout.getExerciseWorkouts();
            exerciseWorkouts.add(new ExerciseWorkout(exerciseForm.workout, exerciseForm.exercise));
            exerciseForm.workout.setExerciseWorkouts(exerciseWorkouts);
        }

        redirectAttributes.addFlashAttribute("edit", false);
        redirectAttributes.addFlashAttribute("workout", exerciseForm.workout);
        exerciseService.save(exerciseForm.exercise);

        if (exerciseForm.getEditWorkout()) {
            return "redirect:/workout/edit/" + exerciseForm.workout.getId();
        } else {
            return "redirect:/workout/create";
        }
    }
}
