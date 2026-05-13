package com.smartfinance.userservice;

import com.smartfinance.userservice.dto.RegisterRequest;
import com.smartfinance.userservice.dto.UserResponse;
import com.smartfinance.userservice.entity.Role;
import com.smartfinance.userservice.entity.User;
import com.smartfinance.userservice.exception.UserExistException;
import com.smartfinance.userservice.repository.RoleRepository;
import com.smartfinance.userservice.repository.UserRepository;
import com.smartfinance.userservice.security.CustomUserDetailsService;
import com.smartfinance.userservice.security.JwtService;
import com.smartfinance.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @InjectMocks
    private UserService userService;

    @Test
    public void register_WhenUsernameDoesNotExist_ShouldCreateUser(){
        //Arrange
        RegisterRequest request = new RegisterRequest("christian","123456");
        when(userRepository.findByUsername("christian")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User userToSave = invocation.getArgument(0);
                    userToSave.setId(1L);
                    return userToSave;
                });
        
        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER"))
                .thenReturn(Optional.of(userRole));
        
        //Act
        UserResponse response = userService.register(request);
        //Assert
        assertNotNull(response);
        assertEquals(1L,response.id());
        assertEquals("christian", response.username());
        assertTrue(response.roles().contains("ROLE_USER"));
        assertTrue(response.enabled());

        verify(userRepository).findByUsername("christian");
        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(any(User.class));
        /*verify(userRepository).save(argThat(user ->
                user.getUsername().equals("christian")
                        && user.getPassword().equals("encoded-password")
                        && user.getRole().equals("ROLE_USER")
                        && user.getEnabled()
                        && user.getCreatedAt() != null
        ));*/
        verify(userRepository).save(userCaptor.capture());

        User userCaptured = userCaptor.getValue();
        //assertEquals("christian", userCaptured.getUsername());
        assertThat(userCaptured.getUsername()).isEqualTo("christian");
        //assertEquals("encoded-password", userCaptured.getPassword());
        assertThat(userCaptured.getPassword()).isEqualTo("encoded-password");
        //assertEquals("ROLE_USER", userCaptured.getRole());
        assertTrue(
                userCaptured.getRoles()
                        .stream()
                        .anyMatch(role -> role.getName().equals("ROLE_USER"))
        );
        assertTrue(userCaptured.getEnabled());
        assertNotNull(userCaptured.getCreatedAt());
    }

    @Test
    void register_WhenUsernameAlreadyExists_ShouldThrowException() {
        // Arrange
        RegisterRequest request = new RegisterRequest("christian","password");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("christian");

        when(userRepository.findByUsername("christian"))
                .thenReturn(Optional.of(existingUser));

        // Act & Assert
        UserExistException exception = assertThrows(
                UserExistException.class,
                () -> userService.register(request)
        );

        assertEquals("User "+"christian"+" already exist.", exception.getMessage());

        verify(userRepository).findByUsername("christian");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

}
