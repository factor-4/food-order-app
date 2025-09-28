package com.example.FoodOrderApp.security;

import com.example.FoodOrderApp.auth_users.entity.User;
import com.example.FoodOrderApp.auth_users.repository.UserRepository;
import com.example.FoodOrderApp.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username)
                 .orElseThrow(()-> new NotFoundException("User not Found"));
        return AuthUser.builder()
                .user(user)
                .build();
    }
}
