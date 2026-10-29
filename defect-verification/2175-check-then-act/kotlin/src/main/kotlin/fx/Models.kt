package fx

import jakarta.persistence.*
import org.springframework.data.jpa.repository.*
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Service

@Entity
class Coupon(@Id @GeneratedValue var id: Long? = null, var code: String = "", var status: String = "")

@Entity
class Redemption(@Id @GeneratedValue var id: Long? = null, var code: String = "")

interface CouponRepository : JpaRepository<Coupon, Long> {
    @Modifying
    @Query("update Coupon c set c.status = 'used' where c.id = :id and c.status = 'open'")
    fun markUsedIfOpen(@Param("id") id: Long): Int
}

@Service
class CouponService(private val repo: CouponRepository) {
    fun markUsed(c: Coupon) {
        c.status = "used"
        repo.save(c)
    }
}
