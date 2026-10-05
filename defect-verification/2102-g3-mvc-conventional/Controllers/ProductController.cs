using Microsoft.AspNetCore.Mvc;

public class ProductController : Controller
{
    public IActionResult Index() => Content("list");
    public IActionResult Details(int id) => Content("detail " + id);
    [HttpPost]
    public IActionResult Create(string name) => Content("created " + name);
    [NonAction]
    public string Helper() => "not an action";
}
