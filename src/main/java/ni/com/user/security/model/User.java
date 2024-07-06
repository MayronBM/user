package ni.com.user.security.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {@UniqueConstraint(name = "UK_POST_EMAIL", columnNames = "email")
                , @UniqueConstraint(name = "UK_POST_USER", columnNames = "username")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String username;
    private String email;
    private String password;
    private LocalDateTime created;
    private LocalDateTime modificated;
    private LocalDateTime lastLogin;
    private String token;
    private boolean enabled;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Phone> phones;
}
