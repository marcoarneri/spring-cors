package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.request.UserLoginRequest;
import it.krisopea.springcors.controller.model.request.UserRegistrationRequest;
import it.krisopea.springcors.controller.model.request.UserUpdateRequest;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.mapper.MapperUserEntity;
import it.krisopea.springcors.repository.model.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TemplateController {

  private final UserRepository repository;
  private final MapperUserEntity mapperUserEntity;

  @GetMapping("/login")
  public String showLoginPage(ModelMap model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null
        && !(authentication instanceof AnonymousAuthenticationToken)
        && authentication.isAuthenticated()) {
      return showHomePage(new ModelMap());
    }
    model.addAttribute("userLoginRequest", new UserLoginRequest());
    return "login";
  }

  @GetMapping("/access-denied")
  public String showAccessDeniedPage() {
    return "access-denied";
  }

  @GetMapping("/home")
  public String showHomePage(ModelMap model) {
    model.addAttribute(
        "username", SecurityContextHolder.getContext().getAuthentication().getName());
    return "home";
  }

  @GetMapping("/update")
  public String showUpdateUserPage(ModelMap model) {
    UserEntity user =
        repository
            .findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    UserUpdateRequest updateRequest = mapperUserEntity.toUpdateRequest(user);
    model.addAttribute("userUpdateRequest", updateRequest);
    return "update";
  }

  @GetMapping("/register")
  public String showRegistrationPage(ModelMap model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null
        && !(authentication instanceof AnonymousAuthenticationToken)
        && authentication.isAuthenticated()) {
      return showHomePage(new ModelMap());
    }
    model.addAttribute("userRegistrationRequest", new UserRegistrationRequest());
    return "register";
  }

  @GetMapping("/verify")
  public String showVerificationPage() {
    return "verification";
  }
}
