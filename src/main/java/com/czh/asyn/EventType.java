package com.czh.asyn;

/**
 * Created by Chen on 2017/5/10.
 */
public enum EventType {
    LIKE(0), COMMENT(1), LOGIN(2), MESSAGE(3), DISLIKE(4);

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
