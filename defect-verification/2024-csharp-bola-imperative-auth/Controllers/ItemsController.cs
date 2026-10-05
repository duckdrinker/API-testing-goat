using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace Fixture2024.Controllers;

public class Item
{
    public string Id { get; set; } = "";
    public string OwnerId { get; set; } = "";
}

/// <summary>
/// #2024 item 3 / #2018 (DepsDoctor#4450): C# authentication inferred from an imperative
/// IAuthorizationService.AuthorizeAsync call in the action body (Orchard Core shape).
/// Only Case F uses an [Authorize] attribute, as a control.
/// </summary>
[ApiController]
[Route("api/items")]
public class ItemsController : ControllerBase
{
    private readonly IAuthorizationService _authz;
    public ItemsController(IAuthorizationService authz) => _authz = authz;

    // Case A: imperative POLICY-level check only (no resource passed). Proves the caller is
    // authenticated, not that they own item {id}.
    // Expected: authenticated=true, path param id -> BOLA candidate CONFIRMED.
    [HttpGet("{id}")]
    public async Task<ActionResult<Item>> GetItem(string id)
    {
        if (!(await _authz.AuthorizeAsync(User, "ViewItems")).Succeeded) return Forbid();
        return new Item { Id = id, OwnerId = "u1" };
    }

    // Case B: imperative OBJECT-level check (resource passed to AuthorizeAsync).
    // Expected: authenticated=true; BOLA must NOT fire (mirrors Orchard /api/content/{contentItemId}).
    [HttpGet("owned/{id}")]
    public async Task<ActionResult<Item>> GetOwnedItem(string id)
    {
        var item = new Item { Id = id, OwnerId = "u1" };
        if (!(await _authz.AuthorizeAsync(User, item, "ViewItems")).Succeeded) return Forbid();
        return item;
    }

    // Case C: control, no authentication at all, id path param.
    // Expected: unauthenticated_endpoint only; BOLA not assessable (no auth precondition).
    [HttpGet("public/{id}")]
    public Item GetPublicItem(string id) => new Item { Id = id, OwnerId = "u1" };

    // Case D: imperative check, but keyed by NAME, not an id (Orchard /api/queries/{name}).
    // Expected: authenticated=true; BOLA must NOT fire (no id-shaped param).
    [HttpGet("by-name/{name}")]
    public async Task<ActionResult<Item>> GetItemByName(string name)
    {
        if (!(await _authz.AuthorizeAsync(User, "ViewItems")).Succeeded) return Forbid();
        return new Item { Id = "1", OwnerId = name };
    }

    // Case F: attribute-based control (already worked before #4450).
    // Expected: authenticated=true, BOLA CONFIRMED (same as lang-csharp ProductController.GetProduct).
    [HttpGet("attr/{id}")]
    [Authorize]
    public Item GetAttrItem(string id) => new Item { Id = id, OwnerId = "u1" };
}
