package it.krisopea.springcors.controller.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {
  @NotBlank
  @Size(min = 3, max = 50)
  @Pattern(regexp = "^[A-Za-z0-9]+$")
  private String username;

  @NotBlank
  @Size(min = 6, max = 255)
  private String password;
}
