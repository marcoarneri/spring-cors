package it.krisopea.springcors.controller.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DemoRequest {

    @NotBlank
    @Size(max = 70)
    private String iuv;

    @NotBlank
    @Pattern(regexp = "([A-Z]{2})")
    private String city;

    @NotBlank
    @Pattern(regexp = "([A-Z]{2})")
    private String nation;

    @NotBlank
    @Size(max = 70)
    private String noticeId;
}
