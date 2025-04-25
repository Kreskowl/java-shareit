package ru.practicum.shareit.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ErrorResponse {
    private final int status;
    private final String error;
    private final String message;
    private final List<String> details;

    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = null;
    }

    public ErrorResponse(int status, String error, String message, List<String> details) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
    }
}

