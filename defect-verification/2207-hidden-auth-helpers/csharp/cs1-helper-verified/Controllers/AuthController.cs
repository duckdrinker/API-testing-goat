using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;

namespace Fixture2207.Controllers;

public class AppUser : IdentityUser { }

[ApiController]
[Route("api/auth")]
public class AuthController : ControllerBase
{
    private readonly SignInManager<AppUser> _signInManager;
    private readonly TokenValidationParameters _validation = new();

    public AuthController(SignInManager<AppUser> signInManager) { _signInManager = signInManager; }

    // canonical site: password verified, then signed in
    [HttpPost("login")]
    public async Task Login(string user, string password)
    {
        var result = await _signInManager.PasswordSignInAsync(user, password, false, false);
        await HttpContext.SignInAsync(BuildPrincipal(user));
    }

    private static ClaimsPrincipal BuildPrincipal(string name) =>
        new ClaimsPrincipal(new ClaimsIdentity(new[] { new Claim(ClaimTypes.Name, name) }, "app"));
    private ClaimsPrincipal VerifyToken(string token)
    {
        var handler = new JwtSecurityTokenHandler();
        return handler.ValidateToken(token, _validation, out _);
    }
    [HttpPost("trust")]
    public void Trust()
    {
        var principal = VerifyToken(HttpContext.Request.Headers["X-Token"]);
        HttpContext.User = principal;
    }
}