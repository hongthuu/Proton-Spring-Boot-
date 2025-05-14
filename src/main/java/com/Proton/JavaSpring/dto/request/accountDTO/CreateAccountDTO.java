package com.Proton.JavaSpring.dto.request.accountDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class CreateAccountDTO {

//    @NotBlank(message = "User Name require")
//    private String userName;
//
//    @NotBlank(message = "Password is required")
//    private String password;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "0\\d{9}", message = "Phone number must have exactly 10 digits and start with 0")
    private String phoneNumber;

}
