using System.Threading;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

[ApiController]
[Route("coupons")]
public class CouponsController : ControllerBase
{
    private readonly AppDb db;
    private readonly CouponService service;
    private static readonly object Mutex = new object();

    public CouponsController(AppDb db, CouponService service) { this.db = db; this.service = service; }

    // c1: unguarded check -> write
    [HttpPost("{id}/redeem-unguarded")]
    public IActionResult RedeemUnguarded(int id)
    {
        var c = db.Coupons.Find(id);
        if (c.Status == "open")
        {
            c.Status = "used";
            db.SaveChanges();
        }
        return Ok();
    }

    // c2: check in controller, write in service
    [HttpPost("{id}/redeem-split")]
    public IActionResult RedeemSplit(int id)
    {
        var c = db.Coupons.Find(id);
        if (c.Status == "open")
        {
            service.MarkUsed(c);
        }
        return Ok();
    }

    // c3: atomic conditional UPDATE
    [HttpPost("{id}/redeem-atomic")]
    public IActionResult RedeemAtomic(int id)
    {
        var c = db.Coupons.Find(id);
        if (c.Status == "open")
        {
            db.Database.ExecuteSqlRaw("UPDATE Coupons SET Status = 'used' WHERE Id = {0} AND Status = 'open'", id);
        }
        return Ok();
    }

    // c4: delete of the row just checked
    [HttpPost("{id}/delete-after-check")]
    public IActionResult DeleteAfterCheck(int id)
    {
        var c = db.Coupons.Find(id);
        if (c.Status == "expired")
        {
            db.Coupons.Remove(c);
            db.SaveChanges();
        }
        return Ok();
    }

    // c5a: check and write in the same lock block
    [HttpPost("{id}/lock-same-block")]
    public IActionResult LockSameBlock(int id)
    {
        lock (Mutex)
        {
            var c = db.Coupons.Find(id);
            if (c.Status == "open")
            {
                c.Status = "used";
                db.SaveChanges();
            }
        }
        return Ok();
    }

    // c5b: check in one lock block, write in another
    [HttpPost("{id}/lock-different-blocks")]
    public IActionResult LockDifferentBlocks(int id)
    {
        Coupon c;
        bool open;
        lock (Mutex)
        {
            c = db.Coupons.Find(id);
            open = c.Status == "open";
        }
        if (open)
        {
            lock (Mutex)
            {
                c.Status = "used";
                db.SaveChanges();
            }
        }
        return Ok();
    }

    // c5c: Monitor.Exit before the write
    [HttpPost("{id}/unlock-before-write")]
    public IActionResult UnlockBeforeWrite(int id)
    {
        Monitor.Enter(Mutex);
        var c = db.Coupons.Find(id);
        bool open = c.Status == "open";
        Monitor.Exit(Mutex);
        if (open)
        {
            c.Status = "used";
            db.SaveChanges();
        }
        return Ok();
    }

    // c6: already-done write
    [HttpPost("{id}/already-done-write")]
    public IActionResult AlreadyDoneWrite(int id)
    {
        var c = db.Coupons.Find(id);
        if (c.Status == "used")
        {
            c.Status = "used";
            db.SaveChanges();
        }
        return Ok();
    }
}
