package com.example.assettracker.service;

import com.example.assettracker.domain.Role;
import com.example.assettracker.domain.User;
import com.example.assettracker.dtos.AuthRequest;
import com.example.assettracker.dtos.RegisterRequest;
import com.example.assettracker.repository.UserRepository;
import com.example.assettracker.security.AuthenticationService;
import com.example.assettracker.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void registerAndShouldSaveUserAndReturn() {
        var request = new RegisterRequest("John", "Doe", "johndoe@example.com", "password123", Role.USER);

        when(passwordEncoder.encode(request.password())).thenReturn("hashedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("mock-jwt-token");

        var response = authenticationService.register(request);

        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo("mock-jwt-token");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void authenticateAndShouldReturnTokenWhenCredentialsAreValid() {
        var request = new AuthRequest("johndoe@example.com", "password123");
        var user = new User("johndoe@example.com", "hashedPassword","John", "Doe", Role.USER);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("mock-jwt-token");

        var response = authenticationService.authenticate(request);

        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo("mock-jwt-token");
        verify(authenticationManager, times(1)).authenticate(any());
    }
}
