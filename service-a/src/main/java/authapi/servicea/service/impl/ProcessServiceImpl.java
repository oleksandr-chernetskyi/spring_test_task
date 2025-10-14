package authapi.servicea.service.impl;

import authapi.servicea.dto.process.ProcessRequestDto;
import authapi.servicea.dto.process.ProcessResponseDto;
import authapi.servicea.model.ProcessingLog;
import authapi.servicea.model.User;
import authapi.servicea.repository.ProcessingLogRepository;
import authapi.servicea.repository.UserRepository;
import authapi.servicea.service.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final ProcessingLogRepository processingLogRepository;

    @Value("${internal.token}")
    private String internalToken;

    @Override
    public ProcessResponseDto processText(ProcessRequestDto requestDto, String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found: " + email);
        }
        User user = optionalUser.get();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Internal-Token", internalToken);

        HttpEntity<ProcessRequestDto> requestDtoHttpEntity = new HttpEntity<>(requestDto, httpHeaders);
        ResponseEntity<ProcessResponseDto> responseDtoResponseEntity = restTemplate.postForEntity(
                "http://data-api:8081/api/transform", requestDtoHttpEntity, ProcessResponseDto.class
        );

        if (!responseDtoResponseEntity.getStatusCode().is2xxSuccessful()
                || responseDtoResponseEntity.getBody() == null) {
            throw new RuntimeException("Service B error: " + responseDtoResponseEntity.getStatusCode());
        }

        ProcessResponseDto responseDto = responseDtoResponseEntity.getBody();

        ProcessingLog processingLog = new ProcessingLog();
        processingLog.setUserId(user.getId());
        processingLog.setInputText(requestDto.getText());
        processingLog.setOutputText(responseDto.getResult());
        processingLog.setCreatedAt(Instant.now());
        processingLogRepository.save(processingLog);

        return responseDto;
    }
}
