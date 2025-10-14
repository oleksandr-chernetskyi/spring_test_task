package authapi.servicea.controller;

import authapi.servicea.dto.process.ProcessRequestDto;
import authapi.servicea.dto.process.ProcessResponseDto;
import authapi.servicea.service.ProcessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/process")
public class ProcessController {
    private final ProcessService processService;

    @PostMapping
    public ProcessResponseDto process(@RequestBody @Valid ProcessRequestDto requestDto,
                                      Authentication authentication) {
        String email = authentication.getName();
        log.info("Received process request from user: {} with text: {}", email, requestDto.getText());
        return processService.processText(requestDto, email);
    }
}
