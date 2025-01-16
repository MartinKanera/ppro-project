package org.example.workouttracker.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "workouts")
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Valid
    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseWorkout> exerciseWorkouts = new ArrayList<>();

    public Workout() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ExerciseWorkout> getExerciseWorkouts() {
        return exerciseWorkouts;
    }

    public void setExerciseWorkouts(List<ExerciseWorkout> exerciseWorkouts) {
        this.exerciseWorkouts = exerciseWorkouts;
    }

    public void addEmptyExerciseWorkout() {
        ExerciseWorkout exerciseWorkout = new ExerciseWorkout();
        exerciseWorkout.setWorkout(this);
        exerciseWorkout.setExercise(new Exercise());
        exerciseWorkout.setIndex(this.exerciseWorkouts.size());
        exerciseWorkouts.add(exerciseWorkout);
    }

    public void sortExerciseWorkouts() {
        exerciseWorkouts.sort(Comparator.comparingInt(ExerciseWorkout::getIndex));
    }

    public void removeExerciseWorkout(int index) {

        this.exerciseWorkouts.remove(index);

        for (int i = 0; i < exerciseWorkouts.size(); i++) {
            exerciseWorkouts.get(i).setIndex(i);
        }
    }
}