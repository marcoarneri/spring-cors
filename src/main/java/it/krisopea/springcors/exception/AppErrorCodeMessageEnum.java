package it.krisopea.springcors.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AppErrorCodeMessageEnum {
  ERROR("0500", "system.error", HttpStatus.INTERNAL_SERVER_ERROR),
  BAD_REQUEST("0400", "bad.request", HttpStatus.BAD_REQUEST),
  PASSWORD_MISMATCH("1401", "password.mismatch", HttpStatus.UNAUTHORIZED),
  EMAIL_ALREDY_EXIST("1402", "email.already.exist", HttpStatus.BAD_REQUEST),
  EMAIL_NOT_EXIST("1403", "email.not.exist", HttpStatus.BAD_REQUEST),
  ACCESS_DENIED("1404", "access.denied", HttpStatus.FORBIDDEN),
  USER_EXISTS("1409", "user.exists", HttpStatus.CONFLICT),
  USER_NOT_EXISTS("1410", "user.not.exists", HttpStatus.BAD_REQUEST),
  PASSWORD_IS_OLD("14011", "password.is.old", HttpStatus.BAD_REQUEST);

  private final String errorCode;
  private final String errorMessageKey;
  private final HttpStatus httpStatus;

  AppErrorCodeMessageEnum(String errorCode, String errorMessageKey, HttpStatus httpStatus) {
    this.errorCode = errorCode;
    this.errorMessageKey = errorMessageKey;
    this.httpStatus = httpStatus;
  }
}
