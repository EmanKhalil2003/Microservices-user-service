package com.e_commerce.user_service.controller;

import com.e_commerce.user_service.dto.AuthResponse;
import com.e_commerce.user_service.dto.LoginRequest;
import com.e_commerce.user_service.dto.SignupRequest;
import com.e_commerce.user_service.entity.Role;
import com.e_commerce.user_service.entity.User;
import com.e_commerce.user_service.repository.UserRepository;
import com.e_commerce.user_service.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {

        // get fields from request body
        String name = signupRequest.getName();
        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();
        Role role = signupRequest.getRole();

        // check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists!");
        }

        // get hashed password
        String encodedPassword = passwordEncoder.encode(password);

        // create and save user
        User user = new User(name, email, encodedPassword, role);
        userRepository.save(user);

        // return success response
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

        // looks for the user by email
        Optional<User> potentialUser = userRepository.findByEmail(loginRequest.getEmail());

        if (potentialUser.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password!");
        }

        User user = potentialUser.get();
        // compares entered password with stored hashed password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password!");
        }

        String token = jwtService.generateToken(user.getId(), user.getRole());

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
