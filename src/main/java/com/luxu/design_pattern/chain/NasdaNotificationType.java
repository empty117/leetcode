package com.luxu.design_pattern.chain;

/**
 * Created by xulu on 11/16/2017.
 */
/**
 * Kafka agent change notification eventType
 */
public enum NasdaNotificationType {
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
