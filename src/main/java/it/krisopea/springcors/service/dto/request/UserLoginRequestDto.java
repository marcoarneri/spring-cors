package it.krisopea.springcors.service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDto {
  private String usernameOrEmail;

  private String password;
}
