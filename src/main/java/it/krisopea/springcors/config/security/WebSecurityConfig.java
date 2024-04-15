package it.krisopea.springcors.config.security;

import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.util.constant.PrivilegeEnum;
import it.krisopea.springcors.util.constant.RoleConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final UserRepository userRepository;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.sessionManagement(
            sessionManagementConfigurer ->
                sessionManagementConfigurer.maximumSessions(1).maxSessionsPreventsLogin(true))
        .authorizeHttpRequests(
            requests ->
                requests
                    .requestMatchers("/admin")
                    .hasAnyAuthority(RoleConstants.ROLE_ADMIN, RoleConstants.ROLE_FOUNDER)
                    .requestMatchers("/admin/update/**")
                    .hasAuthority(String.valueOf(PrivilegeEnum.WRITE))
                    .requestMatchers("/admin/delete/**")
                    .hasAuthority(String.valueOf(PrivilegeEnum.DELETE))
                    .requestMatchers("/entry", "/register")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(
            form ->
                form.loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/home", true)
                    .failureUrl("/login?error")
                    .permitAll())
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(
                        (request, response, authentication) -> {
                          SecurityContextHolder.clearContext();
                          request.getSession().invalidate();
                          response.sendRedirect("/entry?success");
                        })
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .permitAll())
        .exceptionHandling(
            exception ->
                exception.accessDeniedHandler(
                    (request, response, accessDeniedException) -> {
                      request.setAttribute("error", accessDeniedException);
                      request.setAttribute("requestUri", request.getRequestURI());
                      request.getRequestDispatcher("/access-denied").forward(request, response);
                    }));

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  //    @Bean
  //    public UserDetailsService userDetailsService() {
  //      return username ->
  //          userRepository
  //              .findByUsername(username)
  //              .map(
  //                  user ->
  //                      User.builder()
  //                          .username(user.getUsername())
  //                          .authorities()
  //                          .roles()
  //                          .password(user.getPassword())
  //                          .build())
  //              .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not
  // found"));
  //    }

  @Bean
  public AuthenticationManager authenticationManager(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder);

    return new ProviderManager(authenticationProvider);
  }

  //  /* -- Role hierarchy -- */
  //  @Bean
  //  public RoleHierarchy roleHierarchy() {
  //    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
  //    String hierarchy =
  //        RoleConstants.ROLE_FOUNDER
  //            + ">"
  //            + RoleConstants.ROLE_ADMIN
  //            + "\n"
  //            + RoleConstants.ROLE_ADMIN
  //            + ">"
  //            + RoleConstants.ROLE_USER;
  //    roleHierarchy.setHierarchy(hierarchy);
  //    return roleHierarchy;
  //  }
  //
  //  @Bean
  //  public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler() {
  //    DefaultWebSecurityExpressionHandler expressionHandler =
  //        new DefaultWebSecurityExpressionHandler();
  //    expressionHandler.setRoleHierarchy(roleHierarchy());
  //    return expressionHandler;
  //  }

  @Bean
  public static ServletListenerRegistrationBean httpSessionEventPublisher() {
    return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
  }
}
