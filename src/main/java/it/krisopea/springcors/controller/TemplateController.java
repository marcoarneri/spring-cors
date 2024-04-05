package it.krisopea.springcors.controller;

import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TemplateController {

  private final UserService userService;

  @GetMapping("/login")
  public String loginPage() {
    return "login";
  }

  @GetMapping("/logout")
  public String profilePage() {
    SecurityContextHolder.clearContext();
    return "login";
  }

  @GetMapping("/entry")
  public String entryPage(OAuth2AuthenticationToken authenticationToken, Model model) {
    UserEntity userEntity = userService.saveUser(authenticationToken);
    model.addAttribute("userEntity", userEntity);
    return "entry";
  }

  @GetMapping("/login/error")
  public String loginErrorPage() {
    return "login";
  }
}
