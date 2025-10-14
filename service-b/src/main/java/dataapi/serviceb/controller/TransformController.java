package dataapi.serviceb.controller;

import dataapi.serviceb.dto.TransformRequestDto;
import dataapi.serviceb.dto.TransformResponseDto;
import dataapi.serviceb.service.TransformService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class TransformController {
    private final TransformService transformService;

    @Value("${internal.token}")
    private String internalToken;

    @PostMapping("/transform")
    public ResponseEntity<TransformResponseDto> transformResponseDtoResponseEntity(
            @RequestHeader(value = "X-Internal-Token", required = false) String token,
            @Valid @RequestBody TransformRequestDto requestDto) {
        if (token == null || !token.equals(internalToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        TransformResponseDto responseDto = transformService.transformResponseDto(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
