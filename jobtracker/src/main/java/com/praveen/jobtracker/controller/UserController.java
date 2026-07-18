package com.praveen.jobtracker.controller;

import java.security.Principal;
import com.praveen.jobtracker.entity.User;
import com.praveen.jobtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Open Registration Page
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Open Login Page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Save User
    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user) {

        userService.saveUser(user);

        return "redirect:/register?success";
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {

        User user = userService.findByEmail(principal.getName());

        model.addAttribute("user", user);

        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User user,
                                RedirectAttributes redirectAttributes) {

        userService.updateProfile(user);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Profile updated successfully!"
        );

        return "redirect:/profile";
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage() {
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {

        String message = userService.changePassword(
                principal.getName(),
                currentPassword,
                newPassword,
                confirmPassword
        );

        if (message.equals("Password updated successfully!")) {
            redirectAttributes.addFlashAttribute("successMessage", message);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", message);
        }

        return "redirect:/change-password";
    }



}