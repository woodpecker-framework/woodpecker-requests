package me.gv7.woodpecker.requests.config;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Random;

public class HttpConfigManager {
    private static ProxyConfig proxyConfig = new ProxyConfig();
    private static TimeoutConfig timeoutConfig = new TimeoutConfig();
    private static UserAgentConfig userAgentConfig = new UserAgentConfig();
    private static CustomHttpHeaderConfig customHttpHeaderConfig = new CustomHttpHeaderConfig();


    public static void setProxyConfig(boolean enable,String protocol,String host,int port,String username,String password){
        proxyConfig.setEnable(enable);
        proxyConfig.setProtocol(protocol);
        proxyConfig.setHost(host);
        proxyConfig.setPort(port);
        proxyConfig.setUsername(username);
        proxyConfig.setPassword(password);
    }

    public static ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    public static void setTimeoutConfig(int defaultTimeout,boolean enableMandatoryTimeout,int mandatoryTimeout){
        timeoutConfig.setDefaultTimeout(defaultTimeout);
        timeoutConfig.setEnableMandatoryTimeout(enableMandatoryTimeout);
        timeoutConfig.setMandatoryTimeout(mandatoryTimeout);
    }

    public static TimeoutConfig getTimeoutConfig() {
        return timeoutConfig;
    }

    public static void setUserAgentConfig(LinkedList<String> userAgentList){
        userAgentConfig.setUserAgentList(userAgentList);
    }

    public static UserAgentConfig getUserAgentConfig(){
        return userAgentConfig;
    }

    public static String getUserAgent(){
        LinkedList<String> userAgentList = userAgentConfig.getUserAgentList();
        if(userAgentList.size() == 0){
            return null;
        }
        int n = new Random().nextInt(userAgentList.size());
        return userAgentList.get(n);
    }

    public static void setCustomHttpHeaderConfig(LinkedHashMap<String,String> customHttpHeaders,boolean overwriteHttpHeader){
        customHttpHeaderConfig.setCustomHttpHeaders(customHttpHeaders);
        customHttpHeaderConfig.setOverwriteHttpHeader(overwriteHttpHeader);
    }

    public static CustomHttpHeaderConfig getCustomHttpHeaderConfig() {
        return customHttpHeaderConfig;
    }
}
