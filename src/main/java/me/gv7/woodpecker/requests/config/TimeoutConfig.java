package me.gv7.woodpecker.requests.config;

public class TimeoutConfig {
    private int defaultTimeout;
    private boolean enableMandatoryTimeout;
    private int mandatoryTimeout;

    public int getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(int defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public boolean isEnableMandatoryTimeout() {
        return enableMandatoryTimeout;
    }

    public void setEnableMandatoryTimeout(boolean enableMandatoryTimeout) {
        this.enableMandatoryTimeout = enableMandatoryTimeout;
    }

    public int getMandatoryTimeout() {
        return mandatoryTimeout;
    }

    public void setMandatoryTimeout(int mandatoryTimeout) {
        this.mandatoryTimeout = mandatoryTimeout;
    }
}
