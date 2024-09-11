package com.Java_instagram_clone.domain.auth.controller;

import com.Java_instagram_clone.config.MessageSender;
import com.Java_instagram_clone.domain.auth.service.AuthService;
import com.Java_instagram_clone.domain.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final MessageSender messageSender;
    private final AuthService authService;

    @GetMapping("/emailValidation")
    public ResponseEntity<?> emailValidation(@RequestParam String email) {

        return authService.emailValidation(email);
    }

    @GetMapping("/accountNameValidation")
    public ResponseEntity<?> accountNameValidation(@RequestParam String accountName) {

        return authService.accountNameValidation(accountName);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Member user) {
        // validation check
        return authService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Member user, HttpServletResponse response) {
        return authService.login(user, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return authService.logout(request);
    }

}
