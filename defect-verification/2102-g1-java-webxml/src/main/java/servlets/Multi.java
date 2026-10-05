package servlets;

import java.io.IOException;
import javax.servlet.http.*;

public class Multi extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException { resp.getWriter().write("multi"); }
}
