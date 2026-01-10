package org.example.controller;

import org.example.dto.*;
import org.example.mapper.UserMapper;
import org.example.model.ApplicationUserDetails;
import org.example.model.Role;
import org.example.model.User;
import org.example.service.JwtService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService; // Your service that loads User by username
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<User> createUSer(@RequestBody CreateUserDTO request) {
        User user = userService.registerUser(request.username, request.password, Role.valueOf(request.role));
        if (user == null) {
            throw new IllegalArgumentException("Failed to create user");
        }
        return ResponseEntity.ok(user);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUSer(@RequestBody DeleteUserDTO request) {
        userService.deleteUser(request.id);
        return ResponseEntity.ok(void.class);
    }

    @PatchMapping
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDTO request) {
        User user = UserMapper.INSTANCE.toEntity(request);

        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserDTO request) {
        User user = userService.registerUser(request.username, request.password, Role.USER);
        if (user == null) {
            throw new IllegalArgumentException("Failed to register user");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        // Authenticate (triggers your UserDetailsService)

//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.username(),
//                        request.password()
//                )
//        );

        // Load user and generate token
        User user = userService.authenticate(request.username(), request.password());


        ApplicationUserDetails userDetails = new ApplicationUserDetails(user);
        String jwtToken = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }

    @PostMapping("/validate-role")
    public ResponseEntity<?> validateRole(@RequestHeader(name = "Authorization") String token) {

        String jwtToken = token.substring(7);

        String username = jwtService.extractUsername(jwtToken);
        if (username == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        return new ResponseEntity<>(user, HttpStatus.OK);

    }
}
