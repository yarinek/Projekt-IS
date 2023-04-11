package com.laby.projektkrypto.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.laby.projektkrypto.validation.FieldMatch;
import com.laby.projektkrypto.validation.PasswordStrength;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldMatch(firstField = "password", secondField = "matchingPassword", message = "Passwords do not match")
public class UserRegistration
{
    @NotBlank(message = "Blank not allowed")
    private String username;

    @NotBlank(message = "Blank not allowed")
    @Size(max = 72, message = "Your password cannot be longer than 72 characters")
    @PasswordStrength
    private String password;

    @NotBlank(message = "Blank not allowed")
    @Size(max = 72, message = "Your password cannot be longer than 72 characters")
    private String matchingPassword;

    @NotBlank(message = "Blank not allowed")
    @Email(message = "Invalid email")
    private String email;
}
