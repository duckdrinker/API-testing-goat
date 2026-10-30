package fx

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/coupons")
class CouponController(private val repo: CouponRepository, private val service: CouponService) {
    private val mutex = Any()

    // c1: unguarded check -> write
    @PostMapping("/{id}/redeem-unguarded")
    fun redeemUnguarded(@PathVariable id: Long): String {
        val c = repo.findById(id).orElseThrow()
        if (c.status == "open") {
            c.status = "used"
            repo.save(c)
        }
        return "ok"
    }

    // c2: check in controller, write in service
    @PostMapping("/{id}/redeem-split")
    fun redeemSplit(@PathVariable id: Long): String {
        val c = repo.findById(id).orElseThrow()
        if (c.status == "open") {
            service.markUsed(c)
        }
        return "ok"
    }

    // c3: atomic conditional UPDATE
    @PostMapping("/{id}/redeem-atomic")
    fun redeemAtomic(@PathVariable id: Long): String {
        val c = repo.findById(id).orElseThrow()
        if (c.status == "open") {
            repo.markUsedIfOpen(id)
        }
        return "ok"
    }

    // c4: delete of the row just checked
    @PostMapping("/{id}/delete-after-check")
    fun deleteAfterCheck(@PathVariable id: Long): String {
        val c = repo.findById(id).orElseThrow()
        if (c.status == "expired") {
            repo.delete(c)
        }
        return "ok"
    }

    // c5a: same synchronized block
    @PostMapping("/{id}/sync-same-block")
    fun syncSameBlock(@PathVariable id: Long): String {
        synchronized(mutex) {
            val c = repo.findById(id).orElseThrow()
            if (c.status == "open") {
                c.status = "used"
                repo.save(c)
            }
        }
        return "ok"
    }

    // c5b: different synchronized blocks
    @PostMapping("/{id}/sync-different-blocks")
    fun syncDifferentBlocks(@PathVariable id: Long): String {
        var open = false
        var c: Coupon? = null
        synchronized(mutex) {
            c = repo.findById(id).orElseThrow()
            open = c!!.status == "open"
        }
        if (open) {
            synchronized(mutex) {
                c!!.status = "used"
                repo.save(c!!)
            }
        }
        return "ok"
    }

    // c6: already-done write
    @PostMapping("/{id}/already-done-write")
    fun alreadyDoneWrite(@PathVariable id: Long): String {
        val c = repo.findById(id).orElseThrow()
        if (c.status == "used") {
            c.status = "used"
            repo.save(c)
        }
        return "ok"
    }
}
