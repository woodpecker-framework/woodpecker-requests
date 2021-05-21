package me.gv7.woodpecker.requests.config;

import java.util.LinkedHashMap;

public class CustomHttpHeaderConfig {
    private LinkedHashMap<String,String> customHttpHeaders = new LinkedHashMap<String,String>();
    private boolean overwriteHttpHeader = false;

    public LinkedHashMap<String, String> getCustomHttpHeaders() {
        return customHttpHeaders;
    }

    public void setCustomHttpHeaders(LinkedHashMap<String, String> customHttpHeaders) {
        this.customHttpHeaders = customHttpHeaders;
    }

    public boolean isOverwriteHttpHeader() {
        return overwriteHttpHeader;
    }

    public void setOverwriteHttpHeader(boolean overwriteHttpHeader) {
        this.overwriteHttpHeader = overwriteHttpHeader;
    }
}
