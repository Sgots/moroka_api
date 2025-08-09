package com.springboot.backend.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Data
public class LoginRequest implements Serializable {
    private String email;
    private String password;
}
