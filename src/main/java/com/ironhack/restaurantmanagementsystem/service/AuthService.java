package com.ironhack.restaurantmanagementsystem.service;
import com.ironhack.restaurantmanagementsystem.dto.request.UserRequest;
import com.ironhack.restaurantmanagementsystem.entity.User;
import com.ironhack.restaurantmanagementsystem.enums.RoleName;
import com.ironhack.restaurantmanagementsystem.exception.ConflictException;
import com.ironhack.restaurantmanagementsystem.exception.ResourceNotFoundException;
import com.ironhack.restaurantmanagementsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found: " + email));
    }
    @Transactional
    public User register(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(
                    "User with email " + request.getEmail() + " already exists"
            );
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        RoleName role = RoleName.CUSTOMER;

        user.setRole(role);

        return userRepository.save(user);
    }
}
