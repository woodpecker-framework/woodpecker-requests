package me.gv7.woodpecker.requests.executor;

import me.gv7.woodpecker.requests.Interceptor;
import me.gv7.woodpecker.requests.RawResponse;
import me.gv7.woodpecker.requests.Request;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Http executor
 *
 * @author Liu Dong
 */
public interface HttpExecutor extends Interceptor.InvocationTarget {
    /**
     * Process the request, and return response
     */
    @NonNull
    RawResponse proceed(Request request);
}
