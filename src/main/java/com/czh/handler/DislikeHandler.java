package com.czh.handler;

import com.czh.asyn.EventModel;
import com.czh.asyn.EventType;
import com.czh.entity.Message;
import com.czh.entity.News;
import com.czh.entity.User;
import com.czh.service.MessageService;
import com.czh.service.NewsService;
import com.czh.service.UserService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Chen on 2017/5/10.
 */
@Component
public class DislikeHandler implements EventHandler {
    @Autowired
    private NewsService newsService;

    @Override
    public void doHandler(EventModel eventModel) {
//        System.out.println("DislikeHandler doSomething");
//        System.out.println(ToStringBuilder.reflectionToString(eventModel));
        newsService.updateLikeCount(eventModel.getEntityId(),
                Integer.valueOf(eventModel.getExt("likeCount")));
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.DISLIKE);
    }
}
