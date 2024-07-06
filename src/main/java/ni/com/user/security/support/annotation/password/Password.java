package ni.com.user.security.support.annotation.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordConstraintsValidator.class)
public @interface Password {

  String message() default "Clave Inválida!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
