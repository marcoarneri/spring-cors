package it.krisopea.springcors.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
//                .formLogin(
//                        form ->
//                                form
//                                        .loginPage("/login")
//                                        .permitAll())
                .oauth2Login(
                        oauth2Login ->
                                oauth2Login
                                        .defaultSuccessUrl("/entry")
                                        .failureUrl("/login?error=true")
                )
                .logout(
                        logout ->
                                logout
                                        .invalidateHttpSession(true)
                                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
