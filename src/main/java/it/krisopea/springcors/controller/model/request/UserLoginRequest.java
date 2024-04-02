package it.krisopea.springcors.controller.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {
  @NotBlank
  @Size(max = 255)
  private String usernameOrEmail;

  @NotBlank
  @Size(min = 6, max = 255)
  private String password;
}
