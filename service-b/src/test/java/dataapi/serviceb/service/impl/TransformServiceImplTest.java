package dataapi.serviceb.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import dataapi.serviceb.dto.TransformRequestDto;
import dataapi.serviceb.dto.TransformResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransformServiceImplTest {
    private TransformServiceImpl transformService;

    @BeforeEach
    void setUp() {
        transformService = new TransformServiceImpl();
    }

    @Test
    @DisplayName("transformResponseDto_ValidInput_ReturnsTransformedResponse")
    void transformResponseDtoValidInputReturnsTransformedResponse() {
        TransformRequestDto transformRequestDto = new TransformRequestDto();
        transformRequestDto.setText("hello");

        TransformResponseDto transformResponseDto = transformService
                .transformResponseDto(transformRequestDto);

        assertThat(transformResponseDto).isNotNull();
        assertThat(transformResponseDto.getResult()).isEqualTo("OLLEH");
    }

    @Test
    @DisplayName("transformResponseDto_NullText_ThrowsNullPointerException")
    void transformResponseDtoNullTextThrowsNullPointerException() {
        TransformRequestDto transformRequestDto = new TransformRequestDto();
        transformRequestDto.setText(null);

        assertThatThrownBy(() -> transformService
                .transformResponseDto(transformRequestDto))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Cannot invoke");
    }
}
