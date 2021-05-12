package me.gv7.woodpecker.requests.mock;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * @author Liu Dong {@literal <im@dongliu.net>}
 */
public class MockGetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        String queryStr = request.getQueryString();

        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            sb.append(headerName).append('=');
            Enumeration<String> headerValues = request.getHeaders(headerName);
            while (headerValues.hasMoreElements()) {
                String headerValue = headerValues.nextElement();
                sb.append(headerValue).append(';');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append('\n');
        }
        sb.deleteCharAt(sb.length() - 1);
        String headers = sb.toString();

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        // cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                response.addCookie(cookie);
            }
        }

        PrintWriter out = response.getWriter();

        switch (uri) {
            case "/redirect":
                response.sendRedirect("/redirected");
                break;
            default:
                out.println(uri);
                out.println(queryStr);
                out.println(sb.toString());
        }
    }
}
