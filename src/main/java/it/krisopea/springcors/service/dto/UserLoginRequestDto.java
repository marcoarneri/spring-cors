package it.krisopea.springcors.service.dto;

import lombok.Data;


@Data
public class UserLoginRequestDto {

    private String iuv;
    private String location;
    private String noticeId;
}
