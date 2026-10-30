package servlets;

import java.io.IOException;
import javax.servlet.http.*;

public class Setup extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException { resp.getWriter().write("setup"); }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException { resp.getWriter().write(req.getParameter("name")); }
}
