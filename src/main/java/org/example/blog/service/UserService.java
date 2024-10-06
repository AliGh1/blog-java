package org.example.blog.service;

import org.example.blog.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDetails loadUserByUsername(String username);
    void saveUser(User user);
}
