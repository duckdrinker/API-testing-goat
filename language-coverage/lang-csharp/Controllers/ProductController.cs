using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace LangCSharp.Controllers;

/// <summary>ASEC-072 (BOLA, Phase B C#/ASP.NET source confirmation).</summary>
[ApiController]
[Route("api/products")]
public class ProductController : ControllerBase
{
    // Check absent: authenticated (GET) /resource/{id} shape, no ownership check -> confirmed candidate.
    [HttpGet("{id}")]
    [Authorize]
    public string GetProduct(string id) => $"{{...product {id}...}}";

    // Check present: [Authorize(Policy="OwnerOrAdmin")] -> Phase B clears the candidate.
    [HttpGet("safe/{id}")]
    [Authorize(Policy = "OwnerOrAdmin")]
    public string GetOwnProduct(string id) => $"{{...own product {id}...}}";

    // Role check present, but "Manager" was removed from the shipped privilegedRoles
    // default on broken_object_level_authorization.yml (xygeni-product-backlog#1400) ->
    // Phase B should now confirm this candidate instead of clearing it.
    [HttpGet("manager/{id}")]
    [Authorize(Roles = "Manager")]
    public string GetManagerScopedProduct(string id) => $"{{...product {id}, manager view...}}";

    // xygeni-product-backlog#1551: [Authorize(Roles = "User")] is a bare role
    // check (proves privilege level), not an ownership check -> Phase B should
    // still confirm BOLA here instead of clearing it.
    [HttpGet("role/{id}")]
    [Authorize(Roles = "User")]
    public string GetRoleScopedProduct(string id) => $"{{...role-scoped product {id}...}}";
}
