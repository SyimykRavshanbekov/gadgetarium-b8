package com.example.gadgetariumb8.db.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageUrlValidator.class)
@Documented
public @interface ImageUrlValid {

    String message() default "Недопустимое расширение файла изображения. Допустимые расширения файлов: {allowedExtensions}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] allowedExtensions() default {".jpg", ".jpeg", ".png"};
}
