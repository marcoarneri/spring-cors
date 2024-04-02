package it.krisopea.springcors.service.dto;

import lombok.Data;


@Data
public class UserRegistrationRequestDto {

    private String iuv;
    private String location;
    private String noticeId;
}
