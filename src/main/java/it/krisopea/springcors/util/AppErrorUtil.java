package it.krisopea.springcors.util;

import it.krisopea.springcors.controller.advice.model.ApiErrorResponse;
import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AppErrorUtil {
  private final MessageSource messageSource;

  public Pair<HttpStatus, ApiErrorResponse> buildApiErrorResponse(
          AppException appEx, String errorId, List<ApiErrorResponse.ErrorMessage> errorMessageList) {
    Locale locale = LocaleContextHolder.getLocale();
    AppErrorCodeMessageEnum codeMessage = appEx.getCodeMessage();
    HttpStatus status = codeMessage.getHttpStatus();
    String errorMessageKey = codeMessage.getErrorMessageKey();

    return Pair.of(
        status,
        ApiErrorResponse.builder()
            .errorId(errorId)
            .timestamp(Instant.now())
            .httpStatusCode(status.value())
            .httpStatusDescription(status.getReasonPhrase())
            .appErrorCode(
                String.format("%s-%s", "DEMO", codeMessage.getErrorCode()))
            .message(messageSource.getMessage(errorMessageKey, appEx.getArgs(), locale))
            .errors(errorMessageList)
            .build());
  }
}