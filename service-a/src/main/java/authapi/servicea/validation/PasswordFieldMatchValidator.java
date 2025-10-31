package authapi.servicea.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class PasswordFieldMatchValidator
        implements ConstraintValidator<PasswordFieldMatch, Object> {
    private String field;
    private String fieldMatch;

    public void initialize(PasswordFieldMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object value,
                           ConstraintValidatorContext constraintValidatorContext) {
        Object fieldValue = new BeanWrapperImpl(value)
                .getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(value)
                .getPropertyValue(fieldMatch);

        if (fieldMatch == null || fieldMatchValue == null) {
            return false;
        }
        return Objects.equals(fieldValue, fieldMatchValue);
    }
}
