package ni.com.user.security.support.annotation.password;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ni.com.user.security.support.exception.InvalidRequestParameterException;
import org.passay.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

public class PasswordConstraintsValidator implements ConstraintValidator<Password, String> {


    private static final String PASSAY_MESSAGE_FILE_PATH = "src/main/resources/messages.properties";

    private final HttpServletRequest httpServletRequest;

    public PasswordConstraintsValidator(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {

        PasswordValidator passwordValidator = new PasswordValidator(generateMessageResolver(),
                Arrays.asList(
                        //Length rule. Min 10 max 128 characters
                        new LengthRule(10, 128),

                        //At least one upper case letter
                        new CharacterRule(EnglishCharacterData.UpperCase, 1),

                        //At least one lower case letter
                        new CharacterRule(EnglishCharacterData.LowerCase, 1),

                        //At least one number
                        new CharacterRule(EnglishCharacterData.Digit, 1),

                        //At least one special characters
                        new CharacterRule(EnglishCharacterData.Special, 1),

                        new WhitespaceRule(MatchBehavior.Contains)
                )
        );

        RuleResult result = passwordValidator.validate(new PasswordData(password));

        if (result.isValid()) {
            return true;
        }

        //Sending one message each time failed validation.
        constraintValidatorContext.buildConstraintViolationWithTemplate(passwordValidator.getMessages(result).stream().findFirst()
                        .orElse(constraintValidatorContext.getDefaultConstraintMessageTemplate()))
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }

    private MessageResolver generateMessageResolver() {
        Properties props = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(String.format(PASSAY_MESSAGE_FILE_PATH))) {

            props.load(new InputStreamReader(fileInputStream, StandardCharsets.ISO_8859_1));
            return new PropertiesMessageResolver(props);

        } catch (IOException exception) {
            throw new InvalidRequestParameterException("Invalid language parameter!");
        }
    }
}
