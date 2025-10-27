package authapi.servicea.service.impl;

import authapi.servicea.dto.process.ProcessRequestDto;
import authapi.servicea.dto.process.ProcessResponseDto;
import authapi.servicea.model.User;
import authapi.servicea.repository.ProcessingLogRepository;
import authapi.servicea.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessServiceImplTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProcessingLogRepository processingLogRepository;

    @InjectMocks
    private ProcessServiceImpl processService;

    private ProcessRequestDto processRequestDto;
    private ProcessResponseDto processResponseDto;
    private User testUser;

    @BeforeEach
    void setUp() {
        processRequestDto = new ProcessRequestDto();
        processRequestDto.setText("Hello!");

        processResponseDto = new ProcessResponseDto();
        processResponseDto.setResult("Processed Text");

        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setEmail("test@mail.com");
        testUser.setPassword("encodedPassword");
    }

    @Test
    @DisplayName("processText_ValidUserAndResponse_ReturnsProcessedResult")
    void processTextValidUserAndResponseReturnsProcessedResult() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(restTemplate.postForEntity(anyString(),
                any(HttpEntity.class),
                eq(ProcessResponseDto.class)))
                .thenReturn(new ResponseEntity<>(processResponseDto, HttpStatus.OK));

        ProcessResponseDto result = processService.processText(processRequestDto, testUser.getEmail());

        assertThat(result).isNotNull();
        assertThat(result.getResult()).isEqualTo("Processed Text");

        verify(userRepository).findByEmail(testUser.getEmail());
        verify(restTemplate).postForEntity(anyString(),
                any(HttpEntity.class),
                eq(ProcessResponseDto.class));
        verify(processingLogRepository).save(any());
    }

    @Test
    @DisplayName("processText_UserNotFound_ThrowsRuntimeException")
    void processTextUserNotFoundThrowsRuntimeException() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> processService.processText(processRequestDto, testUser.getEmail()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");

        verify(userRepository).findByEmail(testUser.getEmail());
        verifyNoInteractions(restTemplate);
    }
}
