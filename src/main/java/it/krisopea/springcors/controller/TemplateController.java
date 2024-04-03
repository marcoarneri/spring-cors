package it.krisopea.springcors.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class TemplateController {

  //  @GetMapping(value = "/login")
  //  public ModelAndView getLoginPage(ModelMap map) {
  //    UserRegistrationRequest user = new UserRegistrationRequest();
  //    map.addAttribute("user", user);
  //    return new ModelAndView("register_form", map);
  //  }
  //
  //  @PostMapping(value = "/home")
  //  public ModelAndView getHome(String username, String password, ModelMap map) {
  //    map.addAttribute("username", username);
  //    map.addAttribute("password", password);
  //    return new ModelAndView("home", map);
  //  }
  //
  //  @GetMapping("/register")
  //  public String showForm(Model model) {
  //    UserRegistrationRequest user = new UserRegistrationRequest();
  //    model.addAttribute("user", user);
  //    return "register_form";
  //  }
  //
  //  @PostMapping("/register")
  //  public String submitForm(@ModelAttribute("user") UserRegistrationRequest user) {
  //    System.out.println(user);
  //    return "home";
  //  }
}
