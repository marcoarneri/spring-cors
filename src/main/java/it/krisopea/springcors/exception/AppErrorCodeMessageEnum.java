package it.krisopea.springcors.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AppErrorCodeMessageEnum {
  ERROR("0500", "system.error", HttpStatus.INTERNAL_SERVER_ERROR),
  BAD_REQUEST("0400", "bad.request", HttpStatus.BAD_REQUEST),
  PASSWORD_MISMATCH("1401", "password.mismatch", HttpStatus.UNAUTHORIZED),
  EMAIL_ALREDY_EXIST("1402", "email.already.exist", HttpStatus.BAD_REQUEST),
  USER_EXISTS("1409", "user.exists", HttpStatus.CONFLICT);

  private final String errorCode;
  private final String errorMessageKey;
  private final HttpStatus httpStatus;

  AppErrorCodeMessageEnum(String errorCode, String errorMessageKey, HttpStatus httpStatus) {
    this.errorCode = errorCode;
    this.errorMessageKey = errorMessageKey;
    this.httpStatus = httpStatus;
  }
}
