package com.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public String handleNotFound(NotFoundException ex, Model model, HttpServletResponse response) {
    response.setStatus(HttpStatus.NOT_FOUND.value());
    model.addAttribute("message", ex.getMessage());
    return "error/404";
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public String handleNoHandler(NoHandlerFoundException ex, Model model, HttpServletResponse response) {
    response.setStatus(HttpStatus.NOT_FOUND.value());
    model.addAttribute("message", "La page demandée n'existe pas: " + ex.getRequestURL());
    return "error/404";
  }

  @ExceptionHandler(Exception.class)
  public String handleGeneric(Exception ex, Model model, HttpServletResponse response) {
    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    model.addAttribute("message", ex.getMessage());
    return "error/500";
  }
}
