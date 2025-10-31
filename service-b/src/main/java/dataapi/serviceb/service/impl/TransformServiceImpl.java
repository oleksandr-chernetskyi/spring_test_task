package dataapi.serviceb.service.impl;

import dataapi.serviceb.dto.TransformRequestDto;
import dataapi.serviceb.dto.TransformResponseDto;
import dataapi.serviceb.service.TransformService;
import org.springframework.stereotype.Service;

@Service
public class TransformServiceImpl implements TransformService {
    @Override
    public TransformResponseDto transformResponseDto(TransformRequestDto transformRequestDto) {
        String result = new StringBuilder(transformRequestDto
                .getText()
                .toUpperCase())
                .reverse()
                .toString();
        return new TransformResponseDto(result);
    }
}
