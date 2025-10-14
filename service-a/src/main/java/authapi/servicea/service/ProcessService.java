package authapi.servicea.service;

import authapi.servicea.dto.process.ProcessRequestDto;
import authapi.servicea.dto.process.ProcessResponseDto;

public interface ProcessService {
    ProcessResponseDto processText(ProcessRequestDto requestDto, String email);
}
