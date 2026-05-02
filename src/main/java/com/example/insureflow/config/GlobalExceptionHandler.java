package com.example.insureflow.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, Model model) {
        String fieldName = ex.getName();
        model.addAttribute("message", "Invalid value for " + fieldName + ". Please enter a valid value and try again.");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model) {
        model.addAttribute("message", ex.getMessage() != null ? ex.getMessage() : "An unexpected server error occurred.");
        return "error";
    }
}
