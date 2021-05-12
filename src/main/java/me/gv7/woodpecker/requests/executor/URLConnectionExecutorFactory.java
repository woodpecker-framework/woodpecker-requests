package me.gv7.woodpecker.requests.executor;

/**
 * Only for internal use
 */
class URLConnectionExecutorFactory extends RequestExecutorFactory {
    static final RequestExecutorFactory instance = new URLConnectionExecutorFactory();

    @Override
    public SessionContext newSessionContext() {
        return new SessionContext(new DefaultCookieJar());
    }

    @Override
    public HttpExecutor getHttpExecutor() {
        return new URLConnectionExecutor();
    }
}
