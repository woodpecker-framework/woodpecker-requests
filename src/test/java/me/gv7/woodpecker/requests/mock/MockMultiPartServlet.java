package me.gv7.woodpecker.requests.mock;

import net.dongliu.commons.io.InputStreams;
import org.eclipse.jetty.server.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @author Liu Dong {@literal <im@dongliu.net>}
 */
public class MockMultiPartServlet extends HttpServlet {

    private static final MultipartConfigElement MULTI_PART_CONFIG = new MultipartConfigElement(System.getProperty
            ("java.io.tmpdir"));

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);

        Collection<Part> parts = request.getParts();

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        OutputStream out = response.getOutputStream();
        for (Part part : parts) {
            out.write(part.getName().getBytes(StandardCharsets.UTF_8));
            out.write('\n');
            if (part.getContentType() != null) {
                out.write(part.getContentType().getBytes(StandardCharsets.UTF_8));
                out.write('\n');
            }
            try (InputStream in = part.getInputStream()) {
                InputStreams.transferTo(in, out);
            }
            out.write('\n');
        }
    }
}
