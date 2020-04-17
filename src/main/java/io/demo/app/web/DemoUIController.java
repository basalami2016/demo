package io.demo.app.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Controller
public class DemoUIController {

    @Autowired
    WebClient webClient;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping (value = "/demo")
    public String demo(Model model) {
        //String[] auth = authn();
        String[] message = {"azure", "devops", "demo"};
        model.addAttribute("messages", message);
        return "index";
    }

    private String[] authn() {
        log.info("devops-demo");
        return this.webClient
                .get()
                .uri("/devops-demo/user")
                .retrieve()
                .bodyToMono(String[].class)
                .block();
    }
}
