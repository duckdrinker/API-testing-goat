public class CouponService
{
    private readonly AppDb db;
    public CouponService(AppDb db) { this.db = db; }

    public void MarkUsed(Coupon c)
    {
        c.Status = "used";
        db.SaveChanges();
    }
}
