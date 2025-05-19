package org.oneedtech.eduapi.bootcamp;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Resource Server Security Configuration This configuration class sets up the security filter
 * chains for the resource server.
 */
@Configuration
@EnableWebSecurity
public class ResourceServerSecurityConfig {
  /**
   * Require authorization for all requests to "/ims/**" and scope "articles.read"
   *
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  @Order(1)
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/ims/**")
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .anyRequest()
                    .hasAnyAuthority(
                        "SCOPE_http://purl.1edtech.org/spec/eduapi/v1p0/scope/core.readonly",
                        "SCOPE_http://purl.1edtech.org/spec/eduapi/v1p0/scope/core.readonly.privacy"))
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));
    return http.build();
  }
}
