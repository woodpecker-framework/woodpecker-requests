package me.gv7.woodpecker.requests.mock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author Liu Dong {@literal <im@dongliu.net>}
 */
public class MockPostServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        Map<String, String[]> params = request.getParameterMap();

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println(uri);
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            out.println(entry.getKey() + "=" + entry.getValue()[0]);
        }
    }
}
