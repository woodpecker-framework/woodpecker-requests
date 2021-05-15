package me.gv7.woodpecker.requests.config;

import java.util.HashMap;
import java.util.Map;

public class CustomHttpHeaderConfig {
    private static Map<String,String> CUSTOM_HTTP_HEADERS = new HashMap<String,String>();
    private static boolean overwriteHttpHeader = false;

    public static Map<String, String> getCustomHttpHeaders() {
        return CUSTOM_HTTP_HEADERS;
    }

    public static void setCustomHttpHeaders(Map<String, String> customHttpHeaders) {
        CUSTOM_HTTP_HEADERS = customHttpHeaders;
    }

    public static boolean isOverwriteHttpHeader() {
        return overwriteHttpHeader;
    }

    public static void setOverwriteHttpHeader(boolean overwriteHttpHeader) {
        CustomHttpHeaderConfig.overwriteHttpHeader = overwriteHttpHeader;
    }
}
