using System;
using System.Web.UI;

namespace Demo
{
    public partial class Orders : Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            Response.Write("hi");
        }

        protected void Save_Click(object sender, EventArgs e)
        {
            Response.Write("saved");
        }
    }
}
