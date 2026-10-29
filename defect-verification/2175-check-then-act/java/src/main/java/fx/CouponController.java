package fx;

import java.util.concurrent.locks.ReentrantLock;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupons")
public class CouponController {
    private final CouponRepository repo;
    private final CouponService service;
    private final Object mutex = new Object();
    private final ReentrantLock lock = new ReentrantLock();

    public CouponController(CouponRepository repo, CouponService service) {
        this.repo = repo;
        this.service = service;
    }

    // c1: unguarded check -> write in one handler
    @PostMapping("/{id}/redeem-unguarded")
    public String redeemUnguarded(@PathVariable Long id) {
        Coupon c = repo.findById(id).orElseThrow();
        if (c.getStatus().equals("open")) {
            c.setStatus("used");
            repo.save(c);
        }
        return "ok";
    }

    // c2: check in controller, write in service
    @PostMapping("/{id}/redeem-split")
    public String redeemSplit(@PathVariable Long id) {
        Coupon c = repo.findById(id).orElseThrow();
        if (c.getStatus().equals("open")) {
            service.markUsed(c);
        }
        return "ok";
    }

    // c3: atomic conditional UPDATE ... WHERE status = 'open'
    @PostMapping("/{id}/redeem-atomic")
    public String redeemAtomic(@PathVariable Long id) {
        Coupon c = repo.findById(id).orElseThrow();
        if (c.getStatus().equals("open")) {
            repo.markUsedIfOpen(id);
        }
        return "ok";
    }

    // c4: delete of the row just checked
    @PostMapping("/{id}/delete-after-check")
    public String deleteAfterCheck(@PathVariable Long id) {
        Coupon c = repo.findById(id).orElseThrow();
        if (c.getStatus().equals("expired")) {
            repo.delete(c);
        }
        return "ok";
    }

    // c5a: check and write in the same synchronized block
    @PostMapping("/{id}/sync-same-block")
    public String syncSameBlock(@PathVariable Long id) {
        synchronized (mutex) {
            Coupon c = repo.findById(id).orElseThrow();
            if (c.getStatus().equals("open")) {
                c.setStatus("used");
                repo.save(c);
            }
        }
        return "ok";
    }

    // c5b: check in one synchronized block, write in another
    @PostMapping("/{id}/sync-different-blocks")
    public String syncDifferentBlocks(@PathVariable Long id) {
        Coupon c;
        boolean open;
        synchronized (mutex) {
            c = repo.findById(id).orElseThrow();
            open = c.getStatus().equals("open");
        }
        if (open) {
            synchronized (mutex) {
                c.setStatus("used");
                repo.save(c);
            }
        }
        return "ok";
    }

    // c5c: unlock before the write
    @PostMapping("/{id}/unlock-before-write")
    public String unlockBeforeWrite(@PathVariable Long id) {
        lock.lock();
        Coupon c = repo.findById(id).orElseThrow();
        boolean open = c.getStatus().equals("open");
        lock.unlock();
        if (open) {
            c.setStatus("used");
            repo.save(c);
        }
        return "ok";
    }

    // c6: "already done" write: row already used, write re-sets the same state
    @PostMapping("/{id}/already-done-write")
    public String alreadyDoneWrite(@PathVariable Long id) {
        Coupon c = repo.findById(id).orElseThrow();
        if (c.getStatus().equals("used")) {
            c.setStatus("used");
            repo.save(c);
        }
        return "ok";
    }
}
