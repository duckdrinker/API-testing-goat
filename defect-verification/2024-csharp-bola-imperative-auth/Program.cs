using Microsoft.AspNetCore.Authorization;
using Fixture2024.Controllers;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddControllers();
builder.Services.AddAuthorization(options =>
{
    options.AddPolicy("ViewItems", p => p.RequireAuthenticatedUser());
});

var app = builder.Build();
app.MapControllers();

// Case E (xygeni-product-backlog#2157): Orchard Core's /api/content/{contentItemId} shape.
// Minimal API, .AllowAnonymous() on the route, authorization done inside the handler.
// Known open gap: still reported as unauthenticated_endpoint (info).
app.MapGet("/api/minimal/items/{itemId}", async (
        string itemId,
        HttpContext http,
        IAuthorizationService authz) =>
    {
        var item = new Item { Id = itemId, OwnerId = "u1" };
        var result = await authz.AuthorizeAsync(http.User, item, "ViewItems");
        return result.Succeeded ? Results.Ok(item) : Results.Forbid();
    })
    .AllowAnonymous();

app.Run();
