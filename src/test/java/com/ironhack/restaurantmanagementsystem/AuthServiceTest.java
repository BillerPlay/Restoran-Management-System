package com.ironhack.restaurantmanagementsystem;

import com.ironhack.restaurantmanagementsystem.dto.request.UserRequest;
import com.ironhack.restaurantmanagementsystem.entity.User;
import com.ironhack.restaurantmanagementsystem.enums.RoleName;
import com.ironhack.restaurantmanagementsystem.exception.ConflictException;
import com.ironhack.restaurantmanagementsystem.repository.UserRepository;
import com.ironhack.restaurantmanagementsystem.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterUserSuccessfully() {
        UserRequest request = new UserRequest();
        request.setFullName("Test User");
        request.setEmail("test@mail.com");
        request.setPassword("123456");
        request.setPhoneNumber("123456789");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encoded_pass");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = authService.register(request);

        assertNotNull(result);
        assertEquals("test@mail.com", result.getEmail());
        assertEquals("Test User", result.getFullName());
        assertEquals("encoded_pass", result.getPassword());
        assertEquals(RoleName.CUSTOMER, result.getRole());

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("123456");
    }

    @Test
    void shouldThrowConflictWhenEmailAlreadyExists() {
        UserRequest request = new UserRequest();
        request.setEmail("test@mail.com");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);

        assertThrows(ConflictException.class, () ->
                authService.register(request)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldReturnUserByEmail() {
        User user = new User();
        user.setEmail("test@mail.com");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        User result = authService.getByEmail("test@mail.com");

        assertNotNull(result);
        assertEquals("test@mail.com", result.getEmail());
        verify(userRepository).findByEmail("test@mail.com");
    }

    @Test
    void shouldThrowWhenUserNotFoundByEmail() {
        when(userRepository.findByEmail("notfound@mail.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                authService.getByEmail("notfound@mail.com")
        );
    }
}
