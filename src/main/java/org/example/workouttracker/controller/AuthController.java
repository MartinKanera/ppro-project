package org.example.workouttracker.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.workouttracker.model.User;
import org.example.workouttracker.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(Authentication auth) {

        if (auth != null && auth.isAuthenticated()) return "redirect:/";

        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model, Authentication auth) {

        if (auth != null && auth.isAuthenticated()) return "redirect:/";

        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid User user, BindingResult bindingResult, HttpServletRequest request) {

        if (userService.existsByUsername(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Username taken");
            return "auth/register";
        }

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        String originalPassword = user.getPassword();
        userService.save(user);

        try {
            request.login(user.getUsername(), originalPassword);
        } catch (ServletException e) {
            e.printStackTrace();
            return "redirect:/register";
        }

        return "redirect:/";
    }
}
