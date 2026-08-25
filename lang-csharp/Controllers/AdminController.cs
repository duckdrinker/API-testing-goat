using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace LangCSharp.Controllers;

/// <summary>ASEC-077 (BFLA, Phase B C# source confirmation).</summary>
[ApiController]
[Route("api/admin")]
public class AdminController : ControllerBase
{
    // Check absent: /admin/ path, destructive operation name, generic auth only -> confirmed candidate.
    [HttpPost("users/{id}/lock")]
    [Authorize]
    public void LockUser(string id) { }

    // Check present: [Authorize(Roles="Admin")] -> Phase B clears the candidate.
    [HttpPost("users/{id}/unlock")]
    [Authorize(Roles = "Admin")]
    public void UnlockUser(string id) { }
}
