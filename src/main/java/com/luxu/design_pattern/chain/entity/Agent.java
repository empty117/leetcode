package com.luxu.design_pattern.chain.entity;

/**
 * Created by xulu on 2017/6/28.
 * Agent bean represent ne3s agent
 */
public class Agent{

    private String hostName;
    private String port;
    private boolean isTLS;

    private String userName;
    private String password;

    private byte[] agentKey;

    private String agentDN;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean isTLS() {
        return isTLS;
    }

    public void setTLS(boolean TLS) {
        isTLS = TLS;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getAgentKey() {
        return agentKey;
    }

    public void setAgentKey(byte[] agentKey) {
        this.agentKey = agentKey;
    }

    public String getAgentDN() {
        return agentDN;
    }

    public void setAgentDN(String agentDN) {
        this.agentDN = agentDN;
    }
}
