package authapi.servicea.validation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordFieldMatchValidatorTest {
    private PasswordFieldMatchValidator passwordFieldMatchValidator;

    @BeforeEach
    void setUp() {
        passwordFieldMatchValidator = new PasswordFieldMatchValidator();

        PasswordFieldMatch annotation = new PasswordFieldMatch() {
            @Override
            public String field() {
                return "password";
            }

            @Override
            public String fieldMatch() {
                return "repeatPassword";
            }

            @Override
            public String message() {
                return "Fields values don't match!";
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return PasswordFieldMatch.class;
            }

            @Override
            @SuppressWarnings("unchecked")
            public Class<? extends jakarta.validation.Payload>[] payload() {
                return (Class<? extends jakarta.validation.Payload>[]) new Class[0];
            }
        };
        passwordFieldMatchValidator.initialize(annotation);
    }

    private record PasswordsTestDto(String password, String repeatPassword) {
    }

    @Test
    @DisplayName("isValid_PasswordsMatch_ReturnsTrue")
    void isValidPasswordsMatchReturnsTrue() {
        PasswordsTestDto dto = new PasswordsTestDto("password123", "password123");

        boolean result = passwordFieldMatchValidator.isValid(dto, null);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isValid_PasswordsDoNotMatch_ReturnsFalse")
    void isValidPasswordsDoNotMatchReturnsFalse() {
        PasswordsTestDto dto = new PasswordsTestDto("password123", "123password");

        boolean result = passwordFieldMatchValidator.isValid(dto, null);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isValid_NullFields_ReturnsFalse")
    void isValidNullFieldsReturnsFalse() {
        PasswordsTestDto dto = new PasswordsTestDto(null, null);

        boolean result = passwordFieldMatchValidator.isValid(dto, null);

        assertThat(result).isFalse();
    }
}
