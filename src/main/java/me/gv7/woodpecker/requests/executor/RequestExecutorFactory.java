package me.gv7.woodpecker.requests.executor;

/**
 * Request Client interface
 */
public abstract class RequestExecutorFactory {

    public static RequestExecutorFactory getInstance() {
        return URLConnectionExecutorFactory.instance;
    }

    /**
     * Create new session context
     */
    public abstract SessionContext newSessionContext();

    public abstract HttpExecutor getHttpExecutor();
}
