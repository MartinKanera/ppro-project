package org.example.workouttracker.model;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


@Entity
@Table(name = "exercises")
public class Exercise {

    public enum MuscleGroup {
        CHEST("Chest"),
        BACK("Back"),
        SHOULDERS("Shoulders"),
        LEGS("Legs"),
        ARMS("Arms"),
        ABS("Abs");

        private final String displayName;

        MuscleGroup(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static Map<MuscleGroup, String> getMuscleGroupMap() {
            return Arrays.stream(MuscleGroup.values())
                    .collect(Collectors.toMap(muscleGroup -> muscleGroup, MuscleGroup::getDisplayName));
        }

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private MuscleGroup muscleGroup;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Constructors
    public Exercise() {
    }

    public Exercise(String name) {
        this.name = name;
    }

    // Getters and Setters
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


    public MuscleGroup getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(MuscleGroup muscleGroup) {
        this.muscleGroup = muscleGroup;
    }
}

