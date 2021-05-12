package me.gv7.woodpecker.requests;

import java.io.IOException;
import java.io.InputStream;

/**
 * Handler raw response body, convert to result T
 */
public interface ResponseHandler<T> {

    /**
     * Handler raw response body, convert to result T
     */
    T handle(ResponseInfo responseInfo) throws IOException;

    /**
     * Response info
     */
    class ResponseInfo {
        private String url;
        private int statusCode;
        private Headers headers;
        private InputStream body;

        ResponseInfo(String url, int statusCode, Headers headers, InputStream body) {
            this.url = url;
            this.statusCode = statusCode;
            this.headers = headers;
            this.body = body;
        }

        /**
         * The url after redirect. if no redirect during request, this url is the origin url send.
         * @deprecated use {@link #url()}
         */
        @Deprecated
        public String getUrl() {
            return url;
        }

        /**
         * The response status code
         * @deprecated use {@link #statusCode()}
         */
        @Deprecated
        public int getStatusCode() {
            return statusCode;
        }

        /**
         * The response headers
         * @deprecated use {@link #headers()}
         */
        @Deprecated
        public Headers getHeaders() {
            return headers;
        }

        /**
         * The response input stream
         * @deprecated use {@link #body()}
         */
        @Deprecated
        public InputStream getIn() {
            return body;
        }

        /**
         * The url after redirect. if no redirect during request, this url is the origin url send.
         */
        public String url() {
            return url;
        }

        /**
         * The response status code
         */
        public int statusCode() {
            return statusCode;
        }

        /**
         * The response headers
         */
        public Headers headers() {
            return headers;
        }

        /**
         * The response body as input stream
         */
        public InputStream body() {
            return body;
        }
    }
}
