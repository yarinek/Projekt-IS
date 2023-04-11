package com.laby.projektkrypto.rest;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laby.projektkrypto.dto.UserRegistration;
import com.laby.projektkrypto.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rest")
public class UserController
{
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Integer> registerUser(@Valid @RequestBody UserRegistration userDto) {
        userService.registerUser(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
