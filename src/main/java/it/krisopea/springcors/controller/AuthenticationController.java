package it.krisopea.springcors.controller;

import it.krisopea.springcors.util.annotation.AllowAnonymous;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
@Slf4j
@Validated
@AllowAnonymous
public class AuthenticationController {
}
