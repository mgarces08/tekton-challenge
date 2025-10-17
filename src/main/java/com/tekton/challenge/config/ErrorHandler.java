package com.tekton.challenge.config;

import com.tekton.challenge.service.exception.PercentageUnavailableException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ErrorHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> onValidation(MethodArgumentNotValidException ex) {
    LOG.error(ex.getMessage(), ex);
    Map<String, Object> body = new HashMap<>();
    body.put("error", "validation_error");
    body.put("details", ex.getBindingResult()
                          .getFieldErrors()
                          .stream()
                          .map(fe -> Map.of("field", fe.getField(),
                                                    "message", fe.getDefaultMessage()))
                          .toList());
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(PercentageUnavailableException.class)
  public ResponseEntity<Map<String,Object>> onPercentage(PercentageUnavailableException ex) {
    LOG.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of("error","percentage_unavailable",
                      "message","External percentage not available and no cache present"));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> onConstraint(ConstraintViolationException ex) {
    LOG.error(ex.getMessage(), ex);
    Map<String, Object> body = new HashMap<>();
    body.put("error", "constraint_violation");
    body.put("details", ex.getConstraintViolations()
                          .stream()
                          .map(v -> Map.of("property", v.getPropertyPath().toString(),
                                                          "message", v.getMessage()))
                          .toList());
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> onIllegalArgument(IllegalArgumentException ex) {
    LOG.error(ex.getMessage(), ex);
    return ResponseEntity.badRequest()
                         .body(Map.of("error","bad_request",
                                  "message",ex.getMessage()));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String,Object>> onUnreadable(HttpMessageNotReadableException ex) {
    return ResponseEntity.badRequest()
                         .body(Map.of("error","malformed_json",
                                      "message","Invalid JSON payload"));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> onGeneric(Exception ex) {
    LOG.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                         .body(Map.of("error","internal_error",
                                    "message", "Unexpected error"));
  }
}
