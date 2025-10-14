package dataapi.serviceb.service;

import dataapi.serviceb.dto.TransformRequestDto;
import dataapi.serviceb.dto.TransformResponseDto;

public interface TransformService {
    TransformResponseDto transformResponseDto(TransformRequestDto transformRequestDto);
}
