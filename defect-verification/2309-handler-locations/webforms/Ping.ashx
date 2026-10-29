<%@ WebHandler Language="C#" Class="Demo.Ping" %>
using System.Web;
namespace Demo
{
    public class Ping : IHttpHandler
    {
        public void ProcessRequest(HttpContext context)
        {
            context.Response.Write("pong");
        }
        public bool IsReusable { get { return true; } }
    }
}
