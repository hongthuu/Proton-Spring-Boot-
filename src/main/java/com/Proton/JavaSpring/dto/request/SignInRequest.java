package com.Proton.JavaSpring.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SignInRequest implements Serializable {

    @NotBlank(message = "username must be not blank")
    private String username;

    @NotBlank(message = "password must be not blank")
    private String password;

//    @NotNull(message = "platform must be not null")
//    private Platform platform;
//
//    private String version;
//
//    private String deviceToken;

}
