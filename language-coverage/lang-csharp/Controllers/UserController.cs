using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace LangCSharp.Controllers;

/// <summary>
/// ASEC-054/055 (excessive_data_exposure), ASEC-062/063 (pii_leak_in_response),
/// ASEC-093 (mass_assignment) — C# fixtures.
/// </summary>
[ApiController]
[Route("api/users")]
public class UserController : ControllerBase
{
    // Positive: "Ssn" is never referenced by the request and is sensitivity-tagged
    // -> excessive_data_exposure (054) + pii_leak_in_response (062).
    [HttpGet("profile")]
    public UserProfileResponse GetProfile([FromQuery] string ssn)
    {
        return new UserProfileResponse { Id = "1", Name = "Jane Doe", Email = "jane@example.com", Ssn = ssn };
    }

    // Negative: only operation-relevant fields exposed -> no EDE (055), no pii_leak (063).
    [HttpGet("{id}/summary")]
    [Authorize]
    public UserSummaryResponse GetSummary(string id)
    {
        return new UserSummaryResponse { Id = id, Name = "Jane Doe" };
    }

    // Positive: "Role" binds straight through to the persistent entity -> mass_assignment (093).
    [HttpPut("profile")]
    public UserProfileResponse UpdateProfile([FromBody] UserProfileUpdateRequest update)
    {
        var entity = new User { Name = update.Name, Email = update.Email, Role = update.Role };
        return new UserProfileResponse { Id = entity.Id, Name = entity.Name ?? "", Email = entity.Email ?? "" };
    }

    // Negative: no bindable protected field -> no mass_assignment.
    [HttpPut("{id}/safe-update")]
    [Authorize]
    public UserProfileResponse SafeUpdate(string id, [FromBody] UserProfileSafeUpdateRequest update)
    {
        var entity = new User { Id = id, Name = update.Name, Email = update.Email };
        return new UserProfileResponse { Id = entity.Id, Name = entity.Name ?? "", Email = entity.Email ?? "" };
    }
}

public class UserProfileResponse
{
    public string Id { get; set; } = "";
    public string Name { get; set; } = "";
    public string Email { get; set; } = "";
    public string Ssn { get; set; } = "";
}

public class UserSummaryResponse
{
    public string Id { get; set; } = "";
    public string Name { get; set; } = "";
}

public class UserProfileUpdateRequest
{
    public string? Name { get; set; }
    public string? Email { get; set; }
    public string? Role { get; set; }
}

public class UserProfileSafeUpdateRequest
{
    public string? Name { get; set; }
    public string? Email { get; set; }
}

public class User
{
    public string Id { get; set; } = "";
    public string? Name { get; set; }
    public string? Email { get; set; }
    public string? Role { get; set; }
}
