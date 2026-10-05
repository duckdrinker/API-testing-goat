<%@ WebHandler Language="C#" Class="OWASP.WebGoat.NET.Ping" %>
using System.Web;
namespace OWASP.WebGoat.NET
{
    public class Ping : IHttpHandler
    {
        public void ProcessRequest(HttpContext context) { context.Response.Write("pong"); }
        public bool IsReusable { get { return true; } }
    }
}
