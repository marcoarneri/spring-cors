package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.request.UserLoginRequest;
import it.krisopea.springcors.controller.model.request.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TemplateController {
  @GetMapping("/login")
  public String showLoginForm(ModelMap model) {
    model.addAttribute("userLoginRequest", new UserLoginRequest());
    return "login";
  }

  @GetMapping("/home")
  public String showHomePage() {
    return "home";
  }

  @GetMapping("/register")
  public String showRegistrationForm(ModelMap model) {
    model.addAttribute("userRegistrationRequest", new UserRegistrationRequest());
    return "register";
  }
}
