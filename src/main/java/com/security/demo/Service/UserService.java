package com.security.demo.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import com.security.demo.Entity.User;
import com.security.demo.Repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    // private final JwtService jwtService;

    public User registerUser(User user) {
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User login(User request, HttpSession session) {
        // FIX: BadCredentialsException was unhandled — now caught and re-thrown with a clear message
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            System.out.println("Inside catch block");
            throw new BadCredentialsException("Invalid email or password.");
        }

        SecurityContext context =
            SecurityContextHolder.createEmptyContext();

    context.setAuthentication(authentication);

    SecurityContextHolder.setContext(context);

    session.setAttribute(
            HttpSessionSecurityContextRepository
                .SPRING_SECURITY_CONTEXT_KEY,
            context
    );

    System.out.println("Session ID: " + session.getId());

    return userRepo.findByEmailIgnoreCase(request.getEmail())
        .orElseThrow(() -> new IllegalStateException("User not found after successful authentication."));
        
        // var jwtToken = jwtService.generateTokenWithEmployeeAndPermissions(user, employeeId, permissions);

        // return AuthResponse.builder()
        //         .token(jwtToken)
        //         .role(user.getRole())
        //         .build();
    }
}
