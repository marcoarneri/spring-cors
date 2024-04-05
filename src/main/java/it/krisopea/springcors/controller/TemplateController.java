package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.request.UserLoginRequest;
import it.krisopea.springcors.controller.model.request.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TemplateController {
  @GetMapping("/login")
  public String showLoginForm(ModelMap model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null
        && !(authentication instanceof AnonymousAuthenticationToken)
        && authentication.isAuthenticated()) {
      return showHomePage(new ModelMap());
    }
    model.addAttribute("userLoginRequest", new UserLoginRequest());
    return "login";
  }

  @GetMapping("/home")
  public String showHomePage(ModelMap model) {
    model.addAttribute(
        "username", SecurityContextHolder.getContext().getAuthentication().getName());
    return "home";
  }

  @GetMapping("/register")
  public String showRegistrationForm(ModelMap model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null
        && !(authentication instanceof AnonymousAuthenticationToken)
        && authentication.isAuthenticated()) {
      return showHomePage(new ModelMap());
    }
    model.addAttribute("userRegistrationRequest", new UserRegistrationRequest());
    return "register";
  }
}
