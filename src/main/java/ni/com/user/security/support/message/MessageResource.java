package ni.com.user.security.support.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageResource {

    private final MessageSource messageSource;

    public String getMessage(String msg) {
        return messageSource.getMessage(msg, null, Locale.getDefault());
    }

    public String getMessage(String msg, Object[] args) {
        return messageSource.getMessage(msg, args, Locale.getDefault());
    }
}
