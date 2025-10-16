package com.codifica.chatbot.core.domain.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SafeStringValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface SafeString {
    String message() default "A entrada contém um caractere proibido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
