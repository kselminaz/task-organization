package com.example.taskorganization.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "Username should not be null")
    private String username;

    @NotBlank(message = "Mail should not be null")
    @Email(message = "This field should be mail")
    private String email;

    @NotBlank(message = "Password should not be null")
    @Size(min = 8,message = "Password should be contains at least 8 characters")
    private String password;

    @NotBlank(message = "Confirm password should not be null")
    @Size(min = 8,message = "Confirm Password should be contains at least 8 characters")
    private String confirmPassword;
}
