package ni.com.user.security.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "phones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Phone {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    private String number;
    private Integer cityCode;
    private Integer contryCode;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
