package com.smartfinance.userservice.service;

import com.smartfinance.userservice.dto.LoginRequest;
import com.smartfinance.userservice.dto.LoginResponse;
import com.smartfinance.userservice.dto.RegisterRequest;
import com.smartfinance.userservice.dto.UserResponse;
import com.smartfinance.userservice.entity.Role;
import com.smartfinance.userservice.entity.User;
import com.smartfinance.userservice.exception.UserExistException;
import com.smartfinance.userservice.repository.RoleRepository;
import com.smartfinance.userservice.repository.UserRepository;
import com.smartfinance.userservice.security.CustomUserDetailsService;
import com.smartfinance.userservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            CustomUserDetailsService userDetailsService,
            JwtService jwtService) {
			this.userRepository = userRepository;
			this.roleRepository = roleRepository;
			this.passwordEncoder = passwordEncoder;
			this.authenticationManager = authenticationManager;
			this.userDetailsService = userDetailsService;
			this.jwtService = jwtService;
			}

    public UserResponse register(RegisterRequest request){
        if (userRepository.findByUsername(request.username()).isPresent()){
            throw new UserExistException(request.username());
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        user.getRoles().add(userRole);
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());

        User userSaved = userRepository.save(user);

        return new UserResponse(
        		userSaved.getId(),
        		userSaved.getUsername(),
        		userSaved.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()),
                        userSaved.getEnabled(),
                        userSaved.getCreatedAt()
        );
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token);
    }
}
