package org.example.blog.service;

import lombok.RequiredArgsConstructor;
import org.example.blog.entity.User;
import org.example.blog.exception.CustomValidationException;
import org.example.blog.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public void saveUser(User user) {
        validationUniqueEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    private void validationUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            Map<String, String> errors = new HashMap<>();
            errors.put("name", "email is already taken");
            throw new CustomValidationException(errors);
        }
    }
}
