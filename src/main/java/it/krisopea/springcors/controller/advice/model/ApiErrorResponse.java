package it.krisopea.springcors.controller.advice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@JsonPropertyOrder({
  "errorId",
  "timestamp",
  "httpStatusCode",
  "httpStatusDescription",
  "appErrorCode",
  "message",
  "errors"
})
public class ApiErrorResponse {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String errorId;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC")
  private Instant timestamp;

  private int httpStatusCode;

  private String httpStatusDescription;

  private String appErrorCode;

  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<ErrorMessage> errors;

  @Builder
  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @JsonPropertyOrder({"path", "message"})
  public static class ErrorMessage {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String path;

    private String message;
  }
}
