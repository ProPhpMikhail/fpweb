package app.finplan.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer sort = 1;
    private String currency;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Version
    private Long version;

    @PrePersist
    public void prePersist() {
        if (sort == null || sort == 0) {
            sort = 1;
        }
    }
}
