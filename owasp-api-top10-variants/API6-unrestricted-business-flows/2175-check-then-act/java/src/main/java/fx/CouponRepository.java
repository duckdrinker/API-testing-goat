package fx;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Modifying
    @Query("update Coupon c set c.status = 'used' where c.id = :id and c.status = 'open'")
    int markUsedIfOpen(@Param("id") Long id);
}
