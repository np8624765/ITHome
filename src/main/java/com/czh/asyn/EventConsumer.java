package com.czh.asyn;

import com.czh.handler.EventHandler;
import com.czh.util.JedisAdapter;
import com.czh.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chen on 2017/5/10.
 */
@Component
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private ApplicationContext applicationContext;
    private Map<EventType, List<EventHandler>> registerCenter = new HashMap<>();

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化注册中心
        //获取实现了EventHandler接口的类的所有实例
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (!beans.isEmpty()) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType type : eventTypes) {
                    if (!registerCenter.containsKey(type)) {
                        registerCenter.put(type, new ArrayList<EventHandler>());
                    }
                    // 注册每个事件的处理函数
                    registerCenter.get(type).add(entry.getValue());
                }
            }
        }
        //消费者子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    EventModel eventModel = jedisAdapter.brpopObject(
                            EventModel.class, RedisKeyUtil.getEventQueueKey(), 0);
                    if (!registerCenter.containsKey(eventModel.getEventType())) {
                        logger.error("不能识别的事件");
                        continue;
                    }
                    for (EventHandler handler : registerCenter.get(eventModel.getEventType())) {
                        handler.doHandler(eventModel);
                    }
                }
            }
        }).start();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
