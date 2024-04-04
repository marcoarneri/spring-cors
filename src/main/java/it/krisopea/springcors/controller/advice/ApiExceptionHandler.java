package it.krisopea.springcors.controller.advice;

import it.krisopea.springcors.controller.advice.model.ApiErrorResponse;
import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.util.AppErrorUtil;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  private final AppErrorUtil appErrorUtil;

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    List<ApiErrorResponse.ErrorMessage> errorMessages =
        ex.getAllErrors().stream()
            .map(
                oe ->
                    ApiErrorResponse.ErrorMessage.builder().message(oe.getDefaultMessage()).build())
            .collect(Collectors.toList());
    AppException appEx = new AppException(ex, AppErrorCodeMessageEnum.BAD_REQUEST);
    Pair<HttpStatus, ApiErrorResponse> httpStatusApiErrorResponsePair =
        appErrorUtil.buildApiErrorResponse(appEx, null, errorMessages);
    return ResponseEntity.status(httpStatusApiErrorResponsePair.getLeft())
        .body(httpStatusApiErrorResponsePair.getRight());
  }

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ApiErrorResponse> handleAppException(AppException appEx) {
    Pair<HttpStatus, ApiErrorResponse> httpStatusApiErrorResponsePair =
        appErrorUtil.buildApiErrorResponse(appEx, null, null);
    return ResponseEntity.status(httpStatusApiErrorResponsePair.getLeft())
        .body(httpStatusApiErrorResponsePair.getRight());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
    log.error(String.format("ExceptionHandler: %s", ex.getMessage()), ex);

    AppException appEx = new AppException(ex, AppErrorCodeMessageEnum.ERROR);
    // errorId viene usato solo per i casi di eccezioni non gestite
    String errorId = UUID.randomUUID().toString();
    Pair<HttpStatus, ApiErrorResponse> httpStatusApiErrorResponsePair =
        appErrorUtil.buildApiErrorResponse(appEx, errorId, null);
    return ResponseEntity.status(httpStatusApiErrorResponsePair.getLeft())
        .body(httpStatusApiErrorResponsePair.getRight());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public final ResponseEntity<ApiErrorResponse> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {
    List<ApiErrorResponse.ErrorMessage> errorMessages =
        ex.getConstraintViolations().stream()
            .map(oe -> ApiErrorResponse.ErrorMessage.builder().message(oe.toString()).build())
            .collect(Collectors.toList());
    AppException appEx = new AppException(ex, AppErrorCodeMessageEnum.BAD_REQUEST);
    Pair<HttpStatus, ApiErrorResponse> httpStatusApiErrorResponsePair =
        appErrorUtil.buildApiErrorResponse(appEx, null, errorMessages);
    return ResponseEntity.status(httpStatusApiErrorResponsePair.getLeft())
        .body(httpStatusApiErrorResponsePair.getRight());
  }
}
