package fx;

import jakarta.persistence.*;

@Entity
public class Redemption {
    @Id @GeneratedValue public Long id;
    public String code;
}
