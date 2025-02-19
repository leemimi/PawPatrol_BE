package com.patrol.global.exceptions;



import com.patrol.global.rsData.RsData;
import com.patrol.standard.base.Empty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<RsData<Empty>> handleServiceException(ServiceException e) {

    ErrorCode errorCode = e.getErrorCode();
    RsData<Empty> rsData = e.getRsData();

    return new ResponseEntity<>(
        rsData,
        HttpStatus.valueOf(errorCode.getStatus())
    );
  }
}
