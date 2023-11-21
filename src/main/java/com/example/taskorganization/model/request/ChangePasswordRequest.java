package com.example.taskorganization.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

    @NotBlank(message = "Reset Password code should not be null")
    private String code;

    @NotBlank(message = "Password should not be null")
    @Size(min = 8,message = "Password should be contains at least 8 characters")
    private String password;

    @NotBlank(message = "Confirm password should not be null")
    @Size(min = 8,message = "Confirm Password should be contains at least 8 characters")
    private String confirmPassword;
}
