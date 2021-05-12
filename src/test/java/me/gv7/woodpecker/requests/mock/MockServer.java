package me.gv7.woodpecker.requests.mock;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * @author Liu Dong {@literal <im@dongliu.net>}
 */
public class MockServer {

    private Server server;

    public void start() {
        server = new Server();

        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(8443);
        http_config.setOutputBufferSize(32768);

        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));
        http.setPort(8080);
        http.setIdleTimeout(30000);

        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(this.getClass().getResource("/keystore").toExternalForm());
        sslContextFactory.setKeyStorePassword("123456");
        sslContextFactory.setKeyManagerPassword("123456");

        HttpConfiguration httpsConfig = new HttpConfiguration(http_config);
        httpsConfig.addCustomizer(new SecureRequestCustomizer());

        ServerConnector https = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                new HttpConnectionFactory(httpsConfig));
        https.setPort(8443);
        https.setIdleTimeout(500000);

        server.setConnectors(new Connector[]{http, https});

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(MockGetServlet.class, "/*");
        handler.addServletWithMapping(MockPostServlet.class, "/post");
        handler.addServletWithMapping(MockBasicAuthenticationServlet.class, "/basicAuth");
        handler.addServletWithMapping(MockMultiPartServlet.class, "/multi_part");
        handler.addServletWithMapping(EchoBodyServlet.class, "/echo_body");
        handler.addServletWithMapping(EchoHeaderServlet.class, "/echo_header");

        // Start things up!
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void join() {
        try {
            server.join();
        } catch (InterruptedException ignore) {
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception ignore) {
        }
    }

    public static void main(String[] args) {
        MockServer server = new MockServer();
        server.start();
        server.join();
    }
}
