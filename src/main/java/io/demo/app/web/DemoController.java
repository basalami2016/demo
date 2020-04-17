package io.demo.app.web;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

@RestController
public class DemoController {

    @GetMapping("/user")
    public String[] user(HttpServerRequest request, HttpServerResponse response, Authentication authn) {

        Mono<SecurityContext> auth = ReactiveSecurityContextHolder.getContext();
        auth
           .map( m -> m
              .getAuthentication()
            )
           .subscribe();

        String[] message = {"azure", "devops", "demo"};
        return message;
    }
}
