package it.krisopea.springcors.security;

import it.krisopea.springcors.repository.UserRepository;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final UserRepository userRepository;
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
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/home", true)
                    .failureUrl("/login")
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

  //  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  //    auth.jdbcAuthentication()
  //        .dataSource(dataSource)
  //        .usersByUsernameQuery(
  //            "SELECT username, password, enabled FROM USER_ENTITY WHERE username=?")
  //        .authoritiesByUsernameQuery("SELECT role FROM USER_ENTITY WHERE username=?")
  //        .passwordEncoder(passwordEncoder());
  //  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
            .findByUsername(username)
            .map(
                user ->
                    User.builder()
                        .username(user.getUsername())
                        .authorities(user.getRole())
                        .password(user.getPassword())
                        .accountExpired(!user.isEnabled())
                        .accountLocked(false)
                        .credentialsExpired(false)
                        .disabled(!user.isEnabled())
                        .build())
            .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
      }
    };
  }

  @Bean
  public AuthenticationManager authenticationManager(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder);

    return new ProviderManager(authenticationProvider);
  }
}
