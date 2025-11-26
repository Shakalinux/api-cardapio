package com.shakalinux.apicardaio.controller;

import com.shakalinux.apicardaio.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> notFound(NotFoundException ex) {
        return Map.of(
                "erro", ex.getMessage(),
                "codigo", 404,
                "timestamp", LocalDateTime.now().toString()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> badRequest(BadRequestException ex) {
        return Map.of(
                "erro", ex.getMessage(),
                "codigo", 400,
                "timestamp", LocalDateTime.now().toString()
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> unauthorized(UnauthorizedException ex) {
        return Map.of(
                "erro", ex.getMessage(),
                "codigo", 401,
                "timestamp", LocalDateTime.now().toString()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> erroGeral(Exception ex) {

        return Map.of(
                "erro", ex.getMessage(),
                "codigo", 500,
                "timestamp", LocalDateTime.now().toString()
        );
    }

}
