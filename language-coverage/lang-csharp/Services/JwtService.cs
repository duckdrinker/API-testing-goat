using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using Microsoft.IdentityModel.Tokens;

namespace LangCSharp.Services;

/// <summary>
/// ASEC-086 (jwt_misconfiguration, C#): signature validation is explicitly
/// disabled (ValidateIssuerSigningKey = false, no-op SignatureValidator) -
/// any attacker-crafted token is accepted as valid.
/// </summary>
public class JwtService
{
    private readonly JwtSecurityTokenHandler _handler = new();

    public ClaimsPrincipal Verify(string token)
    {
        var parameters = new TokenValidationParameters
        {
            ValidateIssuerSigningKey = false,
            SignatureValidator = (t, _) => new JwtSecurityToken(t),
        };
        return _handler.ValidateToken(token, parameters, out _);
    }
}
