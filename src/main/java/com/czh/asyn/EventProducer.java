package com.czh.asyn;

import com.czh.util.JedisAdapter;
import com.czh.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Chen on 2017/5/10.
 */
@Component
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean commitEvent(EventModel eventModel) {
        try {
            jedisAdapter.lpushObject(RedisKeyUtil.getEventQueueKey(), eventModel);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
