package it.krisopea.springcors.controller.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
  @Size(max = 50)
  @Pattern(regexp = "^[A-Za-z0-9]+$")
  private String name;

  @Size(max = 50)
  @Pattern(regexp = "^[A-Za-z0-9]+$")
  private String surname;

  @Size(min = 3, max = 50)
  @Pattern(regexp = "^[A-Za-z0-9]+$")
  private String username;

  @Email
  @Size(max = 255)
  private String email;

  @Size(max = 255)
  private String password;

  @Size(max = 255)
  private String oldPassword;
}
