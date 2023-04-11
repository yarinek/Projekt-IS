package com.laby.projektkrypto.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserLogin
{
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
