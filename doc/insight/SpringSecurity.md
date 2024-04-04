# Spring Security
Le query SQL sono comandi utilizzati per interagire con un database relazionale al fine di recuperare, modificare, inserire o eliminare dati. Le query SQL seguono una sintassi specifica e possono essere utilizzate per eseguire una varietà di operazioni sui dati del database.

## Passaggi

Segui questi passaggi per utilizzare Spring Security al tuo progetto Spring Boot:

### 1. Aggiunta delle Dipendenze necessarie

- Per iniziare, aggiungi la dipendenza di Spring Security al file `pom.xml` del tuo progetto:

```xml
  <!-- Dependency per l'utilizzo di Spring Security -->
<dependency>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-starter-security</artifactId>
</dependency>

```
***

### 2. Creazione della classe di configurazione

- Crea una classe di configurazione per Spring Security e annotala con `@EnableWebSecurity`, utilizzata per abilitare la configurazione di sicurezza basata su web nel contesto di un'applicazione Spring Boot. Esempio ([WebSecurityConfig.java](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fsecurity%2FWebSecurityConfig.java)).
```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {}
```
***

### 3. Definizione della SecurityFilterChain
- Definisci il metodo `securityFilterChain`, utilizzato all'interno di una classe di configurazione di Spring Security, per definire la catena dei filtri di sicurezza che verranno applicati alle richieste HTTP ricevute dall'applicazione.
```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests(requests -> requests
                    .requestMatchers("/", "/home").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .loginPage("/login")
                    .permitAll()
            )
            .logout(logout -> logout.permitAll());

    return http.build();
  }
}
```

***

### 4. Recupero delle utenze
Definisci all'interno della classe di configurazione un metodo per il recupero delle utenze:
  1. **JDBC**:  
Il recupero dell'utenza tramite `jdbcAuthentication` è un approccio comune utilizzato in applicazioni web per gestire l'autenticazione degli utenti utilizzando un database relazionale. In questo approccio, le informazioni sull'utente, compresi username e password, vengono memorizzate nel database. È necessario prima configurare DataSource con il DataBase scelto nel proprio `application.properties`.
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase
spring.datasource.username=postgres
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver
```
```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
    private final DataSource dataSource;
    
    //Encoder tramite BCrypt
     @Bean
     public PasswordEncoder passwordEncoder() {
          return new BCryptPasswordEncoder();
     }
     
     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
          auth.jdbcAuthentication()
                  .dataSource(dataSource)
                  .usersByUsernameQuery("SELECT username, password, enabled FROM USER WHERE username=?")
                  .authoritiesByUsernameQuery("SELECT role FROM USER WHERE username=?")
                  .passwordEncoder(passwordEncoder());
     }
}
```


  2. **In-Memory**:  
     `InMemoryUserDetailsManager` di Spring Security implementa `UserDetailsService` per fornire supporto per l'autenticazione basata su nome utente/password archiviata in memoria. InMemoryUserDetailsManager fornisce la gestione di `UserDetails` implementando l'interfaccia `UserDetailsManager`.

```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
     //Encoder tramite BCrypt
     @Bean
     public PasswordEncoder passwordEncoder() {
          return new BCryptPasswordEncoder();
     }
    
     @Bean
     public UserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder) {
          UserDetails user =
                  User.builder()
                          .username("user")
                          .password(passwordEncoder.encode("password"))
                          .roles("USER")
                          .build();
          UserDetails admin =
                  User.builder()
                          .username("admin")
                          .password(passwordEncoder.encode("password"))
                          .roles("USER", "ADMIN")
                          .build();

          return new InMemoryUserDetailsManager(user, admin);
     }
}
```














