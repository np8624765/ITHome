package com.czh.asyn;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chen on 2017/5/10.
 */
public class EventModel {
    //事件类型
    private EventType type;
    //事件发起者
    private int fromId;
    //事件影响者
    private int toId;
    //实体类型
    private int entityType;
    //实体ID
    private int entityId;
    //附加信息
    private Map<String, String> exts = new HashMap<>();

    public EventModel() {
    }

    public EventModel(EventType type) {
        this.type = type;
    }

    public EventModel setEventType(EventType type) {
        this.type = type;
        return this;
    }

    public EventModel setFromId(int fromId) {
        this.fromId = fromId;
        return this;
    }

    public EventModel setToId(int toId) {
        this.toId = toId;
        return this;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public EventType getEventType() {
        return type;
    }

    public int getFromId() {
        return fromId;
    }

    public int getToId() {
        return toId;
    }

    public int getEntityType() {
        return entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public String getExt(String key) {
        return exts.get(key);
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }
}
