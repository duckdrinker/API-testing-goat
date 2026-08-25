var builder = WebApplication.CreateBuilder(args);
builder.Services.AddControllers();
builder.Services.AddAuthorization(options =>
{
    // Used by ProductController's "check present" BOLA endpoint (ASEC-072).
    options.AddPolicy("OwnerOrAdmin", policy => policy.RequireAssertion(_ => true));
});

// ASEC-081 (CORS misconfiguration, C#): wildcard origin combined with
// credentials — the dangerous combination the detector looks for.
builder.Services.AddCors(o => o.AddDefaultPolicy(b => b.AllowAnyOrigin().AllowCredentials()));

var app = builder.Build();
app.UseCors();
app.MapControllers();
app.Run();
