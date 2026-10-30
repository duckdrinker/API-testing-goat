var builder = WebApplication.CreateBuilder(args);
var app = builder.Build();

app.MapGet("/users", Handlers.ListUsers);
app.MapPost("/users", () => Results.Created("/users/1", null));
app.MapGet("/local", LocalHandlers.Get);

app.Run();

public static class LocalHandlers
{
    public static IResult Get()
    {
        return Results.Ok("local");
    }
}
