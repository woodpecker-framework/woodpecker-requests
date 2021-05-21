package me.gv7.woodpecker.requests.config;

import java.util.LinkedList;
import java.util.List;

public class UserAgentConfig {
    private LinkedList<String> userAgentList = new LinkedList<String>();
    public LinkedList<String> getUserAgentList() {
        return userAgentList;
    }

    public void setUserAgentList(LinkedList<String> userAgentList) {
        this.userAgentList = userAgentList;
    }
}
