package servlets;

import java.io.IOException;
import javax.servlet.http.*;

public class AdminPurge extends HttpServlet {
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException { resp.getWriter().write("purged"); }
}
