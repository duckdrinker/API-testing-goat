using System;
using System.Web.UI;

namespace OWASP.WebGoat.NET
{
    public partial class LoginPage : Page
    {
        protected void Page_Load(object sender, EventArgs e) { Response.Write(Request.QueryString["msg"]); }
        protected void ButtonLogOn_Click(object sender, EventArgs e) { Response.Redirect("~/Default.aspx?u=" + Request.Form["User"]); }
    }
}
