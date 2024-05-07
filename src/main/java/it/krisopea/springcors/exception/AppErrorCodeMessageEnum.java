package it.krisopea.springcors.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AppErrorCodeMessageEnum {
  ERROR("0500", "system.error", HttpStatus.INTERNAL_SERVER_ERROR),
  BAD_REQUEST("0400", "bad.request", HttpStatus.BAD_REQUEST),
  USER_NOT_EXISTS("1410", "user.not.exists", HttpStatus.BAD_REQUEST),
  DATA_NOT_EXISTS("1411", "data.not.exists", HttpStatus.BAD_REQUEST);

  private final String errorCode;
  private final String errorMessageKey;
  private final HttpStatus httpStatus;

  AppErrorCodeMessageEnum(String errorCode, String errorMessageKey, HttpStatus httpStatus) {
    this.errorCode = errorCode;
    this.errorMessageKey = errorMessageKey;
    this.httpStatus = httpStatus;
  }
}
