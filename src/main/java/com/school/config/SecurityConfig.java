package com.school.config;

import com.school.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService(UserRepository userRepository) {
    return username -> userRepository.findByUsername(username)
        .map(u -> org.springframework.security.core.userdetails.User
            .withUsername(u.getUsername())
            .password(u.getPasswordHash())
            .roles(u.getRole().name())
            .build())
        .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable: " + username));
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
        .requestMatchers("/login", "/error", "/error/**").permitAll()
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .requestMatchers("/professor/**").hasRole("PROFESSOR")
        .requestMatchers("/student/**").hasRole("STUDENT")
        .anyRequest().authenticated()
      )
      .formLogin(form -> form
        .loginPage("/login")
        .defaultSuccessUrl("/post-login", true)
        .permitAll()
      )
      .logout(logout -> logout
        .logoutSuccessUrl("/login?logout")
        .permitAll()
      );
    return http.build();
  }
}
