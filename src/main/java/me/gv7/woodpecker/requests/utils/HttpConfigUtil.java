package me.gv7.woodpecker.requests.utils;

import me.gv7.woodpecker.requests.RequestBuilder;
import me.gv7.woodpecker.requests.config.CustomHttpHeaderConfig;
import me.gv7.woodpecker.requests.config.ProxyConfig;
import me.gv7.woodpecker.requests.config.TimeoutConfig;
import java.util.Map;

public class HttpConfigUtil {
    public static ProxyConfig getProxyConfig(){
        ProxyConfig proxyConfig = new ProxyConfig();
        try {
            Class clazz = Reflects.loadClass("me.gv7.woodpecker.config.Config");
            boolean enable = (Boolean) Reflects.invokeStaticMethod(clazz,"isEnableProxy",new Class[0],new Object[0]);
            proxyConfig.setEnable(enable);

            String protocol = (String) Reflects.invokeStaticMethod(clazz,"getProxyProtocol",new Class[0],new Object[0]);
            proxyConfig.setProtocol(protocol);

            String host = (String)Reflects.invokeStaticMethod(clazz,"getProxyHost",new Class[0],new Object[0]);
            proxyConfig.setHost(host);

            int port = (Integer) Reflects.invokeStaticMethod(clazz,"getProxyPort",new Class[0],new Object[0]);
            proxyConfig.setPort(port);

            String username = (String)Reflects.invokeStaticMethod(clazz,"getProxyUsername",new Class[0],new Object[0]);
            proxyConfig.setUsername(username);

            String password = (String)Reflects.invokeStaticMethod(clazz,"getProxyPassword",new Class[0],new Object[0]);
            proxyConfig.setPassword(password);
        }catch (Exception e){
            if(RequestBuilder.DEBUG)e.printStackTrace();
            proxyConfig.setEnable(false);
        }
        return proxyConfig;
    }

    public static TimeoutConfig getTimeoutConfig(){
        TimeoutConfig timeoutConfig = new TimeoutConfig();
        try {
            Class clazz = Reflects.loadClass("me.gv7.woodpecker.config.Config");
            int defaultTimeout = (Integer)Reflects.invokeStaticMethod(clazz,"getDefaultTimeout",new Class[0],new Object[0]);
            timeoutConfig.setDefaultTimeout(defaultTimeout);

            Boolean enableMandatoryTimeout = (Boolean)Reflects.invokeStaticMethod(clazz,"isEnableMandatoryTimeout",new Class[0],new Object[0]);
            timeoutConfig.setEnableMandatoryTimeout(enableMandatoryTimeout);

            Integer mandatoryTimeout = (Integer)Reflects.invokeStaticMethod(clazz,"getMandatoryTimeout",new Class[0],new Object[0]);
            timeoutConfig.setMandatoryTimeout(mandatoryTimeout);
        }catch (Exception e){
            timeoutConfig.setDefaultTimeout(0);
            timeoutConfig.setEnableMandatoryTimeout(false);
            timeoutConfig.setMandatoryTimeout(0);
            if(RequestBuilder.DEBUG)e.printStackTrace();
        }
        return timeoutConfig;
    }


    public static String getUserAgent(){
        String userAgent = null;
        try {
            Class clazz = Reflects.loadClass("me.gv7.woodpecker.config.Config");
            userAgent = (String)Reflects.invokeStaticMethod(clazz,"getUserAgent",new Class[0],new Object[0]);
        }catch (Exception e){
            if(RequestBuilder.DEBUG)e.printStackTrace();
        }
        return userAgent;
    }

    public static CustomHttpHeaderConfig getCustomHttpHeaderConfig(){
        CustomHttpHeaderConfig customHttpHeaderConfig = new CustomHttpHeaderConfig();
        try {
            Class clazz = Reflects.loadClass("me.gv7.woodpecker.config.Config");
            boolean overwriteHttpHeader = (Boolean)Reflects.invokeStaticMethod(clazz,"isOverwriteHttpHeader",new Class[0],new Object[0]);
            customHttpHeaderConfig.setOverwriteHttpHeader(overwriteHttpHeader);

            Map<String,String> enableMandatoryTimeout = (Map<String,String>) Reflects.invokeStaticMethod(clazz,"getCustomHttpHeaders",new Class[0],new Object[0]);
            customHttpHeaderConfig.setCustomHttpHeaders(enableMandatoryTimeout);
        }catch (Exception e){
            if(RequestBuilder.DEBUG)e.printStackTrace();
        }
        return customHttpHeaderConfig;
    }
}
