package com.luxu.design_pattern.chain;

import com.luxu.design_pattern.chain.entity.AgentChangeNotification;
import com.luxu.design_pattern.chain.entity.StartRegHandler;

/**
 * @author xulu
 * @date 11/16/2017
 */
public class NasdaNotificationHandler extends BaseHandler<AgentChangeNotification>{

    @Override
    public void handleRequest(AgentChangeNotification agentChangeNotification) {
        System.out.println("handle nasda notification");
        if(getSuccessor()!=null){
            getSuccessor().handleRequest(agentChangeNotification);
        }
    }

    /**
     * nasda change notification eventType
     */
    enum NasdaNotificationType {
        /**
         * create
         */
        CREATE("CREATE"),
        /**
         * update
         */
        UPDATE("UPDATE"),
        /**
         * delete
         */
        DELETE("DELETE");
        private String value;
        private NasdaNotificationType(String value){
            this.value = value;
        }

    }
}
