package com.devsu.bank.advisor;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;
    private String exceptionName;
    private String message;
    private int code;
    private String status;

    public ErrorResponse(Exception exception, HttpStatus httpStatus) {
        this.timestamp = new Date();
        this.exceptionName = exception.getClass().getSimpleName();
        this.message = exception.getMessage();
        this.code = httpStatus.value();
        this.status = httpStatus.name();
    }

    public ErrorResponse(String exceptionName, String message, HttpStatus httpStatus) {
        this.timestamp = new Date();
        this.exceptionName = exceptionName;
        this.message = message;
        this.code = httpStatus.value();
        this.status = httpStatus.name();
    }

}
