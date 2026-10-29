package fx;

import org.springframework.stereotype.Service;

@Service
public class CouponService {
    private final CouponRepository repo;
    public CouponService(CouponRepository repo) { this.repo = repo; }

    public void markUsed(Coupon c) {
        c.setStatus("used");
        repo.save(c);
    }
}
