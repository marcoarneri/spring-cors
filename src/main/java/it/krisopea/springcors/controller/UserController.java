package it.krisopea.springcors.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

  @GetMapping(value = "/login")
  public ModelAndView getLoginPage() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("login");
    return mv;
  }
}
