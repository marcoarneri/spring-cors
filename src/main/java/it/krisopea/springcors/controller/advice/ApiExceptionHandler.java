package it.krisopea.springcors.controller.advice;

import it.krisopea.springcors.controller.advice.model.ApiErrorResponse;
import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.util.AppErrorUtil;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

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

//  @ExceptionHandler(AppException.class)
//  public ResponseEntity<ApiErrorResponse> handleAppException(AppException appEx) {
//    Pair<HttpStatus, ApiErrorResponse> httpStatusApiErrorResponsePair =
//        appErrorUtil.buildApiErrorResponse(appEx, null, null);
//    return ResponseEntity.status(httpStatusApiErrorResponsePair.getLeft())
//        .body(httpStatusApiErrorResponsePair.getRight());
//  }

  @ExceptionHandler(AppException.class)
  public ModelAndView handleException(HttpServletRequest request, AppException ex) {
    ModelAndView modelAndView = new ModelAndView("error");
    Pair<HttpStatus, ApiErrorResponse> httpStatusApiErrorResponsePair =
        appErrorUtil.buildApiErrorResponse(ex, null, null);
    modelAndView.addObject("errorMessage", httpStatusApiErrorResponsePair.getRight().getMessage());
    modelAndView.addObject("errorCause", ex.getCodeMessage());
    modelAndView.addObject("errorCode", httpStatusApiErrorResponsePair.getLeft().value());
    modelAndView.addObject("appErrorCode", httpStatusApiErrorResponsePair.getRight().getAppErrorCode());
    return modelAndView;
  }

}
