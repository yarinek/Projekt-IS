package com.laby.projektkrypto.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laby.projektkrypto.config.UserRole;
import com.laby.projektkrypto.dao.RoleRepository;
import com.laby.projektkrypto.dao.UserRepository;
import com.laby.projektkrypto.dto.UserRegistration;
import com.laby.projektkrypto.entity.Role;
import com.laby.projektkrypto.entity.User;
import com.laby.projektkrypto.exception.BadRequestException;
import com.laby.projektkrypto.exception.InternalException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(UserRegistration userDto) {
        if(userRepository.findByUsername(userDto.getUsername()).isPresent())
            throw new BadRequestException("Username already used!");

        if(userRepository.findByEmail(userDto.getEmail()).isPresent())
            throw new BadRequestException("Email already used!");

        Role role = roleRepository.findByName(UserRole.ROLE_USER).orElseThrow(() ->
                new InternalException("Couldn't find role: " + UserRole.ROLE_USER.name()));

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setRoles(new HashSet<>(List.of(role)));
        userRepository.save(user);

        return user;
    }
}
