package com.tekton.challenge.service.exception;

public class PercentageUnavailableException extends RuntimeException {
  public PercentageUnavailableException(String message) { super(message); }
  public PercentageUnavailableException(String message, Throwable cause) { super(message, cause); }
}
