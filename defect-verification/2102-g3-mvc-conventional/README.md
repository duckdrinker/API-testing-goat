# Gap 3 - ASP.NET Core MVC conventional routing + areas
Expected: GET /Product, /Product/Details/{id}, POST /Product/Create; GET /Admin/Users, POST /Admin/Users/Delete/{id} with role admin inherited from BaseAdminController.
Must NOT be an action: ProductController.Helper ([NonAction]).
