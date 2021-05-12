package me.gv7.woodpecker.requests.utils;

import me.gv7.woodpecker.requests.config.ProxyConfig;
import me.gv7.woodpecker.requests.config.TimeoutConfig;
import java.lang.reflect.Method;

public class HttpConfigUtil {
    public static ProxyConfig getProxyConfig(){
        ProxyConfig proxyConfig = new ProxyConfig();
        try {
            Class clazz = Class.forName("me.gv7.woodpecker.config.Config");
            Method isEnableProxy = clazz.getMethod("isEnableProxy", new Class[0]);
            boolean enable = (Boolean) isEnableProxy.invoke(null);
            proxyConfig.setEnable(enable);

            Method getProxyProtocol = clazz.getMethod("getProxyProtocol", new Class[0]);
            String protocol = (String) getProxyProtocol.invoke(null);
            proxyConfig.setProtocol(protocol);

            Method getProxyHost = clazz.getMethod("getProxyHost", new Class[0]);
            String host = (String) getProxyHost.invoke(null);
            proxyConfig.setHost(host);

            Method getProxyPort = clazz.getMethod("getProxyPort", new Class[0]);
            int port = (Integer) getProxyPort.invoke(null);
            proxyConfig.setPort(port);

            Method getProxyUsername = clazz.getMethod("getProxyUsername", new Class[0]);
            String username = (String) getProxyUsername.invoke(null);
            proxyConfig.setUsername(username);

            Method getProxyPassword = clazz.getMethod("getProxyPassword", new Class[0]);
            String password = (String) getProxyPassword.invoke(null);
            proxyConfig.setPassword(password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return proxyConfig;
    }

    public static TimeoutConfig getTimeoutConfig(){
        TimeoutConfig timeoutConfig = new TimeoutConfig();
        try {
            Class clazz = Class.forName("me.gv7.woodpecker.config.Config");
            Method getDefaultTimeout = clazz.getMethod("getDefaultTimeout", new Class[0]);
            int defaultTimeout = (Integer) getDefaultTimeout.invoke(null);
            timeoutConfig.setDefaultTimeout(defaultTimeout);

            Method isEnableMandatoryTimeout = clazz.getMethod("isEnableMandatoryTimeout", new Class[0]);
            Boolean enableMandatoryTimeout = (Boolean) isEnableMandatoryTimeout.invoke(null);
            timeoutConfig.setEnableMandatoryTimeout(enableMandatoryTimeout);

            Method getMandatoryTimeout = clazz.getMethod("getMandatoryTimeout", new Class[0]);
            Integer mandatoryTimeout = (Integer) getMandatoryTimeout.invoke(null);
            timeoutConfig.setMandatoryTimeout(mandatoryTimeout);
        }catch (Exception e){
            timeoutConfig.setDefaultTimeout(0);
            timeoutConfig.setEnableMandatoryTimeout(false);
            timeoutConfig.setMandatoryTimeout(0);
            e.printStackTrace();
        }
        return timeoutConfig;
    }
}
