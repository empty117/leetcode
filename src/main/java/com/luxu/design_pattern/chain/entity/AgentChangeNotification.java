package com.luxu.design_pattern.chain.entity;

import java.util.Date;

/**
 * @author xulu
 * @date 2017/10/20
 * @apiNote his class represent agent change notification json format
 */
public class AgentChangeNotification {
    private String eventType;
    private Agent agent;
    private Date date;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
