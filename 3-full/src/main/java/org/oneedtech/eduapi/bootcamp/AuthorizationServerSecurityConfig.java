package org.oneedtech.eduapi.bootcamp;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Authorization Server Security Configuration
 * This configuration class sets up the security filter chains for the authorization server.
 *
 */
@Configuration
@EnableWebSecurity
public class AuthorizationServerSecurityConfig {
  @Bean
  @Order(1)
  SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
        .oidc(withDefaults()); // Enable OpenID Connect 1.0
    return http.formLogin(withDefaults()).build();
  }

  /**
   * Require login for all requests not in "/ims/**"
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  @Order(2)
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorizeRequests ->
      authorizeRequests
        .requestMatchers("/ims/**", "/1edtech/eduapi/base/v1p0/discovery").permitAll()
        .anyRequest().authenticated()
    )
    .formLogin(withDefaults());
    return http.build();
  }

  @Bean
  UserDetailsService users() {
    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    UserDetails user =
        User.builder()
            .username("admin")
            .password("password")
            .passwordEncoder(encoder::encode)
            .roles("USER")
            .build();
    return new InMemoryUserDetailsManager(user);
  }
}
