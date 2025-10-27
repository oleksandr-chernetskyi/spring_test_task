package dataapi.serviceb.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dataapi.serviceb.dto.TransformRequestDto;
import dataapi.serviceb.dto.TransformResponseDto;
import dataapi.serviceb.service.TransformService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.lang.reflect.Field;

@ExtendWith(MockitoExtension.class)
class TransformControllerTest {
    @Mock
    private TransformService transformService;

    @InjectMocks
    TransformController transformController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private static final String INTERNAL_TOKEN = "MySecretInternalTokenA2D2";

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(transformController).build();
        objectMapper = new ObjectMapper();

        Field field = TransformController.class.getDeclaredField("internalToken");
        field.setAccessible(true);
        field.set(transformController, INTERNAL_TOKEN);
    }

    @Test
    @DisplayName("transformResponseDtoResponseEntity_ValidToken_ReturnsOkResponse")
    void transformResponseDtoResponseEntityValidTokenReturnsOkResponse() throws Exception {
        TransformRequestDto transformRequestDto = new TransformRequestDto();
        transformRequestDto.setText("testText");

        TransformResponseDto expectedResponse = new TransformResponseDto("TXETTSET");

        when(transformService.transformResponseDto(any(TransformRequestDto.class)))
                .thenReturn(expectedResponse);

        MvcResult mvcResult = mockMvc.perform(post("/transform")
                .header("X-Internal-Token", INTERNAL_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transformRequestDto)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        TransformResponseDto actualResponse = objectMapper
                .readValue(jsonResponse, TransformResponseDto.class);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getResult()).isEqualTo("TXETTSET");
    }

    @Test
    @DisplayName("transformResponseDtoResponseEntity_MissingToken_ReturnsForbidden")
    void transformResponseDtoResponseEntityMissingTokenReturnsForbidden() throws Exception {
        TransformRequestDto transformRequestDto = new TransformRequestDto();
        transformRequestDto.setText("testText");

        mockMvc.perform(post("/transform")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transformRequestDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("transformResponseDtoResponseEntity_InvalidToken_ReturnsForbidden")
    void transformResponseDtoResponseEntityInvalidTokenReturnsForbidden() throws Exception {
        TransformRequestDto transformRequestDto = new TransformRequestDto();
        transformRequestDto.setText("testText");

        mockMvc.perform(post("/transform")
                .header("X-Internal-Token", "WrongToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transformRequestDto)))
                .andExpect(status().isForbidden());
    }
}
