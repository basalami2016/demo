package io.demo.app.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity
public class SecurityConfig {

        @Bean
        public UserDetailsRepositoryReactiveAuthenticationManager reactiveAuthenticationManager() {
            return new UserDetailsRepositoryReactiveAuthenticationManager(mapReactiveUserDetailsServiceBean());
        }

        @Bean
        public MapReactiveUserDetailsService mapReactiveUserDetailsServiceBean() {
            return new MapReactiveUserDetailsService(userDetails());
        }

       @Bean
       SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
          http
                .csrf().disable()
                .cors().disable()
                .authorizeExchange(exchanges ->
                    exchanges
                      .pathMatchers("/index.html", "/devops-demo/**").permitAll()
                      //.matchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                      .anyExchange().authenticated()
                )
               .httpBasic(withDefaults())
               .formLogin(withDefaults());

             /**
                .formLogin(formLogin -> formLogin
                   .loginPage("/login")
                );
              */

          return http.build();
       }

      @Bean
      public Map<String, UserDetails> userDetails() {

            Map<String, UserDetails> user = new HashMap<String, UserDetails>();
            Collection<? extends GrantedAuthority> demoAuthorities;

            //haat
            demoAuthorities = Arrays.asList(
                    new SimpleGrantedAuthority("USER"),
                    new SimpleGrantedAuthority("SECURITY"),
                    new SimpleGrantedAuthority("ADMIN"),
                    new SimpleGrantedAuthority("DBA")
            );

            UserDetails demo = new User("demo", passwordEncoder().encode("demo"), demoAuthorities);
            user.put("demo", demo);
            return user;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            return passwordEncoder;
        }


        public HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository() {
            return new HttpSessionCsrfTokenRepository();

        }
}
