package it.krisopea.springcors.service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDeleteRequestDto {
  private String email;

  private String password;
}
