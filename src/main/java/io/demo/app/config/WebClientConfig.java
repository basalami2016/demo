package io.demo.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.function.Consumer;


@Slf4j
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient stixWebClient() {

        Consumer<List<ExchangeFilterFunction>> filterFn = fn ->  {
             fn.add(logRequest());
             fn.add(logResponse());
             fn.add(logStatus());
        };

        WebClient webClient = WebClient.builder()
               .baseUrl("/devops-demo/")
               //.defaultHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.github.v3+json")
               .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
               .defaultHeader("DEMO", "AZURE DEVOPS DEMO")
               .filters(filterFn)
               .build();
        return  webClient;
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return next.exchange(clientRequest);
        };
    }

    private ExchangeFilterFunction logResponse() {
        return (clientResponse, next) -> {
            log.info("Request: {} {}", clientResponse.body(), clientResponse.url());
            clientResponse.headers()
                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return next.exchange(clientResponse);
        };
    }

    private ExchangeFilterFunction logStatus() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response Status {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
}
