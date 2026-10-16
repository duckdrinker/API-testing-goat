using System.Linq;
using System.Security.Claims;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace Acme;

public class Order
{
    public int Id { get; set; }
    public string Owner { get; set; } = "";
}

public class AppDb : DbContext
{
    public DbSet<Order> Orders => Set<Order>();
}

// #2214 - owner bound through a local variable (ASP.NET Core + EF Core).
// NOT REPORTED cases must stay silent; REPORTED cases must fire
// csharp.user_controlled_primary_key.
[ApiController]
[Route("api/orders")]
public class OrdersController : ControllerBase
{
    private readonly AppDb _db;

    public OrdersController(AppDb db) { _db = db; }

    // CTRL-1 REPORTED: unbounded lookup, baseline positive.
    [HttpGet("ctrl1/{id}")]
    public IActionResult Ctrl1(int id)
    {
        var order = _db.Orders.Find(id);
        return Ok(order);
    }

    // CTRL-2 NOT REPORTED: caller written inline in the bound.
    [HttpGet("ctrl2/{id}")]
    public IActionResult Ctrl2(int id)
    {
        var order = _db.Orders.FirstOrDefault(o => o.Id == id && o.Owner == User.Identity!.Name);
        return Ok(order);
    }

    // A1 NOT REPORTED: bound in the lookup (LINQ method syntax), caller held in a local.
    [HttpGet("a1/{id}")]
    public IActionResult A1(int id)
    {
        var caller = User.Identity!.Name;
        var order = _db.Orders.FirstOrDefault(o => o.Id == id && o.Owner == caller);
        return Ok(order);
    }

    // A1q NOT REPORTED: same, LINQ query syntax.
    [HttpGet("a1q/{id}")]
    public IActionResult A1q(int id)
    {
        var caller = User.Identity!.Name;
        var order = (from o in _db.Orders where o.Id == id && o.Owner == caller select o).FirstOrDefault();
        return Ok(order);
    }

    // A2 REPORTED: twin of A1, the local holds a request value.
    [HttpGet("a2/{id}")]
    public IActionResult A2(int id)
    {
        var caller = Request.Query["owner"].ToString();
        var order = _db.Orders.FirstOrDefault(o => o.Id == id && o.Owner == caller);
        return Ok(order);
    }

    // B1 NOT REPORTED: comparison after the lookup, caller held in a local.
    [HttpGet("b1/{id}")]
    public IActionResult B1(int id)
    {
        var caller = User.Identity!.Name;
        var order = _db.Orders.Find(id);
        if (order == null || order.Owner != caller) return Forbid();
        return Ok(order);
    }

    // B2 REPORTED: twin of B1, the local holds a request value.
    [HttpGet("b2/{id}")]
    public IActionResult B2(int id)
    {
        var caller = Request.Query["owner"].ToString();
        var order = _db.Orders.Find(id);
        if (order == null || order.Owner != caller) return Forbid();
        return Ok(order);
    }

    // C1 REPORTED: local first takes the caller, then is rebound to a request value.
    [HttpGet("c1/{id}")]
    public IActionResult C1(int id)
    {
        var caller = User.Identity!.Name;
        caller = Request.Query["owner"].ToString();
        var order = _db.Orders.FirstOrDefault(o => o.Id == id && o.Owner == caller);
        return Ok(order);
    }

    // C2 REPORTED: rebound local, comparison after the lookup.
    [HttpGet("c2/{id}")]
    public IActionResult C2(int id)
    {
        var caller = User.Identity!.Name;
        caller = Request.Query["owner"].ToString();
        var order = _db.Orders.Find(id);
        if (order == null || order.Owner != caller) return Forbid();
        return Ok(order);
    }

    // D1 REPORTED: loop variable, not assigned once from the caller.
    [HttpGet("d1/{id}")]
    public IActionResult D1(int id)
    {
        Order? found = null;
        foreach (var caller in Request.Query["owners"])
        {
            found = _db.Orders.FirstOrDefault(o => o.Id == id && o.Owner == caller);
        }
        return Ok(found);
    }
}
