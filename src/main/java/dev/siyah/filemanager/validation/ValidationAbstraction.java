package dev.siyah.filemanager.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

public abstract class ValidationAbstraction<T extends Annotation, V> implements ConstraintValidator<T, V> {
    protected boolean skipValidation = false;

    @Override
    public boolean isValid(V v, ConstraintValidatorContext constraintValidatorContext) {
        if (this.skipValidation) {
            return true;
        }

        return this.validate(v, constraintValidatorContext);
    }

    public abstract boolean validate(V payload, ConstraintValidatorContext constraintValidatorContext);
}
