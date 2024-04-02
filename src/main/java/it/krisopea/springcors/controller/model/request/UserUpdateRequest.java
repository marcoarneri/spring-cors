package it.krisopea.springcors.controller.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
  @Size(max = 50)
  private String name;

  @Size(max = 50)
  private String surname;

  @Size(max = 255)
  private String password;

  @NotBlank
  @Size(min = 6, max = 255)
  private String oldPassword;
}
