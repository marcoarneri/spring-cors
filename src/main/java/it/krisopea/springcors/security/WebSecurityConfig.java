package it.krisopea.springcors.security;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final DataSource dataSource;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            requests ->
                requests
                    .requestMatchers("/entry", "/register", "/login")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(
            form ->
                form.loginPage("/login")
                    .loginProcessingUrl("/perform_login")
                    .defaultSuccessUrl("/hello", true)
                    .permitAll())
        .logout(
            logout ->
                logout
                    .logoutSuccessUrl("/entry")
                    .logoutUrl("/perform_logout")
                    .invalidateHttpSession(true)
                    .permitAll());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  //  @Bean
  //  public UserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder) {
  //    UserDetails user =
  //        User.builder()
  //            .username("user")
  //            .password(passwordEncoder.encode("password"))
  //            .roles("USER")
  //            .build();
  //
  //    return new InMemoryUserDetailsManager(user);
  //  }

  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication()
        .dataSource(dataSource)
        .usersByUsernameQuery(
            "SELECT username, password, enabled FROM USER_ENTITY WHERE username=?")
        .authoritiesByUsernameQuery("SELECT role FROM USER_ENTITY WHERE username=?")
        .passwordEncoder(passwordEncoder());
  }
}
