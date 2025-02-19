package com.patrol.global.exceptions;



import com.patrol.global.rsData.RsData;
import com.patrol.standard.base.Empty;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

  private final ErrorCodes errorCodes;

  public ServiceException(ErrorCodes errorCodes) {
    super(errorCodes.getCode() + " : " + errorCodes.getMessage());
    this.errorCodes = errorCodes;
  }

  public RsData<Empty> getRsData() {
    return new RsData<>(errorCodes.getCode(), errorCodes.getMessage());
  }
}
