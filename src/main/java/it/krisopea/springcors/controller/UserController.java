package it.krisopea.springcors.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

  @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
  public String loginPage() {
    return "login";
  }

//  @PostMapping("/login1")
//  public ResponseEntity<UserResponse> login(
//      @ModelAttribute("userLoginRequest") UserLoginRequest request) {
//
//    return ResponseEntity.ok().build();
//  }
}
