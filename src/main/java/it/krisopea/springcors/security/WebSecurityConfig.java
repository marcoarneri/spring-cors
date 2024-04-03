package it.krisopea.springcors.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            authorize ->
                authorize.requestMatchers("/", "/home").permitAll().anyRequest().authenticated())
        .formLogin(form -> form.loginPage("/login").permitAll())
        .logout(logout -> logout.logoutSuccessUrl("/home").permitAll());

    return http.build();
  }

  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
            .inMemoryAuthentication()
            .withUser("admin").password("{noop}admin").roles("ADMIN")
            .and()
            .withUser("user").password("{noop}user").roles("USER");
  }
}
