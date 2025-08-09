package com.springboot.backend.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorHandler {
    private Date timestamp;
    private String message;
    private String path;

}
