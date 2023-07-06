package com.example.contacts.controllers;

import com.example.contacts.AuthRequest;
import com.example.contacts.AuthResponse;
import com.example.contacts.SecretKeyGenerator;
import com.example.contacts.entities.User;
import com.example.contacts.interfaces.UserServiceInterface;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Date;

@RequestMapping(
        path = "/",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = {
                MediaType.APPLICATION_JSON_VALUE
        })
@RestController
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceInterface userService;

    @PostMapping("/sign_up")
    public ResponseEntity<String> registerUser(@RequestBody AuthRequest request) {
        // Validate registration data
        if (!isRegistrationDataValid(request)) {
            return ResponseEntity.badRequest().body("Invalid registration data");
        }

        // Check if the username is already taken
        if (userService.isUsernameTaken(request.getLogin())) {
            return ResponseEntity.badRequest().body("Username already exist");
        }

        // Hash the password using the password encoder
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // Create a new user object with the registration data
        User user = new User(request.getLogin(), hashedPassword);

        // Save the user to the database using the user service
        userService.saveUser(user);

        // Return a success response
        return ResponseEntity.ok("User registered successfully");
    }
    //check if the password is not null, not empty, and its length is greater or equals 8 characters, if all these conditions are met, return true
    private boolean isRegistrationDataValid(AuthRequest request) {
        return request.getLogin() != null && !request.getLogin().isEmpty()
                && request.getPassword() != null && !request.getPassword().isEmpty() && request.getPassword().length() >= 8;
    }

    @PostMapping("/sign_in")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        String login = request.getLogin();
        String password = request.getPassword();

        User user = userService.findByUsername(login);
       if (user != null && passwordEncoder.matches(password, user.getPassword())) {
           String token = generateToken(user);
            return ResponseEntity.ok(new AuthResponse(token));
      } else {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
    }
    private static final String secretKey = SecretKeyGenerator.getInstance().getSecretKey();

    private String generateToken(User user) {
        long expirationTime = 3600000; // Token expiration time in milliseconds - 1 hour

        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        String token = Jwts.builder()
                .setIssuer("contacts")
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }
}
