using System.Linq;
using Microsoft.AspNetCore.Mvc;

namespace Acme;

// #2214 follow-up - two C# forms the caller check does not handle (scanner 6.21.0).
// G1/G2: #2482 (null-forgiving operator). G3-G5: #2483 (local named "caller").
// The expectation is the one of the ticket; the actual 6.21.0 result is noted per case.
[ApiController]
[Route("api/orders-gaps")]
public class OrdersGapsController : ControllerBase
{
    private readonly AppDb _db;

    public OrdersGapsController(AppDb db) { _db = db; }

    // G1 NOT REPORTED: caller written inline with the null-forgiving operator.
    // 6.21.0: REPORTED (the same expression without '!' is not).
    [HttpGet("g1/{id}")]
    public IActionResult G1(int id)
    {
        var order = _db.Orders.FirstOrDefault(o => o.Id == id && o.Owner == User.Identity!.Name);
        return Ok(order);
    }

    // G2 NOT REPORTED: same, caller held in a local.
    // 6.21.0: REPORTED.
    [HttpGet("g2/{id}")]
    public IActionResult G2(int id)
    {
        var owner = User.Identity!.Name;
        var order = _db.Orders.FirstOrDefault(o => o.Id == id && o.Owner == owner);
        return Ok(order);
    }

    // G3 REPORTED: the local is named "caller" but holds a request value.
    // 6.21.0: NOT REPORTED (named "owner" it is reported).
    [HttpGet("g3/{id}")]
    public IActionResult G3(int id)
    {
        var caller = Request.Query["owner"].ToString();
        var order = _db.Orders.FirstOrDefault(o => o.Id == id && o.Owner == caller);
        return Ok(order);
    }

    // G4 REPORTED: same, comparison after the lookup.
    // 6.21.0: NOT REPORTED.
    [HttpGet("g4/{id}")]
    public IActionResult G4(int id)
    {
        var caller = Request.Query["owner"].ToString();
        var order = _db.Orders.Find(id);
        if (order == null || order.Owner != caller) return Forbid();
        return Ok(order);
    }

    // G5 REPORTED: the local is named "caller" and is a request parameter.
    // 6.21.0: NOT REPORTED.
    [HttpGet("g5/{id}")]
    public IActionResult G5(int id, [FromQuery] string caller)
    {
        var order = _db.Orders.Find(id);
        if (order == null || order.Owner != caller) return Forbid();
        return Ok(order);
    }
}
