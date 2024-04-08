package it.krisopea.springcors.service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {
  private String name;

  private String surname;

  private String username;

  private String email;

  private String password;

  private String oldPassword;
}
