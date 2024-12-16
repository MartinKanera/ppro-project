package org.example.workouttracker.service;

import org.example.workouttracker.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDetails loadUserByUsername(String username);
    Boolean existsByUsername(String username);

    void save(User user);
}
