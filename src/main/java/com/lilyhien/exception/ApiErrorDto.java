package com.lilyhien.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

//Spring docs: https://docs.spring.io/spring-boot/3.4/api/java/org/springframework/boot/web/servlet/error/DefaultErrorAttributes.html
@Data
public class ApiErrorDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private int statusCode;
    private String error; //The error reason
    private String message; //The exception message
    private String path; //tells the frontend which endpoint failed

    public ApiErrorDto(HttpStatus httpStatus, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.statusCode = httpStatus.value(); //get int statusCode
        this.error = httpStatus.getReasonPhrase(); //get error reason
        this.message = message;
        this.path = path;
    }
}

