using Microsoft.EntityFrameworkCore;

public class Coupon { public int Id { get; set; } public string Code { get; set; } public string Status { get; set; } }
public class Redemption { public int Id { get; set; } public string Code { get; set; } }

public class AppDb : DbContext
{
    public DbSet<Coupon> Coupons { get; set; }
    public DbSet<Redemption> Redemptions { get; set; }
}
