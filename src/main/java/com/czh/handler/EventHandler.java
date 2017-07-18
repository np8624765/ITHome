package com.czh.handler;

import com.czh.asyn.EventModel;
import com.czh.asyn.EventType;

import java.util.List;

public interface EventHandler {
    //事件处理者处理事件
    void doHandler(EventModel eventModel);
    //获取事件处理者需要处理的事件类型
    List<EventType> getSupportEventTypes();
}