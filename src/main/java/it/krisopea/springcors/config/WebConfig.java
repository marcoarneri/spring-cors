package it.krisopea.springcors.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/home").setViewName("home");
    registry.addViewController("/").setViewName("home");
    registry.addViewController("/login").setViewName("login");
    registry.addViewController("/register").setViewName("register");
    registry.addViewController("/entry").setViewName("entry");
    registry.addViewController("/logout").setViewName("logout");
  }
}