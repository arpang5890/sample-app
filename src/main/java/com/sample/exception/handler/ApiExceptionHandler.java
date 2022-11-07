package com.sample.exception.handler;

import com.sample.exception.InsuffientFundsException;
import com.sample.exception.UserAlreadyExistsException;
import com.sample.exception.UserNotFoundException;
import com.sample.generated.model.v1.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(InsuffientFundsException.class)
  public ResponseEntity<ApiError> insuffientFundsException(InsuffientFundsException apiException) {
    ApiError internalError = new ApiError()
        .message(apiException.getMessage())
        .details(apiException.getMessage());
    return new ResponseEntity<>(internalError, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ApiError> userAlreadyExistsException(
      UserAlreadyExistsException apiException) {
    ApiError internalError = new ApiError()
        .message(apiException.getMessage())
        .details(apiException.getMessage());
    return new ResponseEntity<>(internalError, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ApiError> userNotFoundException(UserNotFoundException apiException) {
    ApiError internalError = new ApiError()
        .code("003")
        .message(apiException.getMessage())
        .details(apiException.getMessage());
    return new ResponseEntity<>(internalError, HttpStatus.BAD_REQUEST);
  }
}
