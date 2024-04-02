package it.krisopea.springcors.controller.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequest {
  @NotBlank
  @Size(min = 3, max = 50)
  @Pattern(regexp = "^[A-Za-z0-9]+$")
  private String name;

  @NotBlank
  @Size(min = 3, max = 50)
  @Pattern(regexp = "^[A-Za-z0-9]+$")
  private String surname;

  @NotBlank
  @Size(min = 3, max = 50)
  @Pattern(regexp = "^[A-Za-z0-9]+$")
  private String username;

  @NotBlank
  @Size(min = 6, max = 255)
  private String password;

  @Email
  @NotBlank
  @Size(max = 255)
  private String email;
}
