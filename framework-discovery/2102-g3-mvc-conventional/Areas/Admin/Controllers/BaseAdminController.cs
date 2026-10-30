using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

[Area("Admin")]
[Authorize(Roles = "admin")]
public abstract class BaseAdminController : Controller { }
