package com.example.billing.Controller;

import com.example.billing.Service.User.AppUserDetailsService;
import com.example.billing.Service.User.UserServiceImpl;
import com.example.billing.Util.JwtUtil;
import com.example.billing.io.AuthRequest;
import com.example.billing.io.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserServiceImpl userService;
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) throws Exception {
        authenticate(request.getEmail(), request.getPassword());
        final UserDetails userDetails = appUserDetailsService.loadUserByUsername(request.getEmail());
        final String jwtToken = jwtUtil.generateToken(userDetails);
        String role = userService.getUserRole(request.getEmail());
        return new AuthResponse(request.getEmail(), role, jwtToken);
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        }catch (DisabledException e) {
            throw new Exception("User disabled");
        }catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or password is incorrect");
        }
    }


    @GetMapping("/debug/token")
    public ResponseEntity<Map<String, Object>> debugToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("error", "No valid Authorization header found"));
        }

        String token = authHeader.substring(7);
        try {

            String email = jwtUtil.extractUsername(token);

            Map<String, Object> tokenInfo = new HashMap<>();
            tokenInfo.put("email", email);
            tokenInfo.put("valid", jwtUtil.isTokenExpired(token));
            return ResponseEntity.ok(tokenInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid token: " + e.getMessage()));
        }
    }


    @PostMapping("/encode")
    public ResponseEntity<String> encodePassword(@RequestBody Map<String, String> request) {
        String password = request.get("password");
        if (password == null || password.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be empty");
        }

        String encodedPassword = passwordEncoder.encode(password);
        return ResponseEntity.ok(encodedPassword);
    }
}