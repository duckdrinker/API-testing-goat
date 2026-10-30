using Microsoft.AspNetCore.Mvc;

public class UsersController : BaseAdminController
{
    public IActionResult Index() => Content("users");
    [HttpPost]
    public IActionResult Delete(int id) => Content("deleted " + id);
}
