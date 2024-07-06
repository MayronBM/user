package ni.com.user.security.support.annotation.password;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({Default.class, Extended.class})
public interface Sequence {
}
