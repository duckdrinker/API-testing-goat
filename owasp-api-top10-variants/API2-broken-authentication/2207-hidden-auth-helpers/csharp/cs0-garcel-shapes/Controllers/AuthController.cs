using System.Security.Claims;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;

namespace Fixture2207.Controllers;

public class AppUser : IdentityUser { }
public interface IAdminTokenRepository { string FindActiveToken(); }

[ApiController]
[Route("api/auth")]
public class AuthController : ControllerBase
{
    private readonly SignInManager<AppUser> _signInManager;
    private readonly IAdminTokenRepository _adminTokenRepository;

    public AuthController(SignInManager<AppUser> signInManager, IAdminTokenRepository repo)
    { _signInManager = signInManager; _adminTokenRepository = repo; }

    // canonical site: password verified, then signed in
    [HttpPost("login")]
    public async Task Login(string user, string password)
    {
        var result = await _signInManager.PasswordSignInAsync(user, password, false, false);
        await HttpContext.SignInAsync(BuildPrincipal(user));
    }

    // expected: reported (non_standard_comparison)
    [HttpPost("elevate")]
    public void Elevate()
    {
        var presented = HttpContext.Request.Headers["X-Admin-Token"];
        var expected = _adminTokenRepository.FindActiveToken();
        if (presented == expected) HttpContext.Session.SetString("uid", "1");
    }

    // expected: reported (unverified)
    [HttpPost("trust")]
    public void Trust()
    {
        HttpContext.User = BuildPrincipal(HttpContext.Request.Headers["X-Remote-User"]);
    }

    // expected: reported (conditional_bypass)
    [HttpPost("maintenance")]
    public void Maintenance()
    {
        if (Environment.GetEnvironmentVariable("AUTH_MODE") != "strict") HttpContext.Session.SetString("uid", "0");
    }

    private static ClaimsPrincipal BuildPrincipal(string name) =>
        new ClaimsPrincipal(new ClaimsIdentity(new[] { new Claim(ClaimTypes.Name, name) }, "app"));
}