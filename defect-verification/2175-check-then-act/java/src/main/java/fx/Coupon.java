package fx;

import jakarta.persistence.*;

@Entity
public class Coupon {
    @Id @GeneratedValue public Long id;
    public String code;
    public String status;
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
}
