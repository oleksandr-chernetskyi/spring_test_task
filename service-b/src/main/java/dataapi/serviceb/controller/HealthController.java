package dataapi.serviceb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "Data API is up and healthy";
    }
}
