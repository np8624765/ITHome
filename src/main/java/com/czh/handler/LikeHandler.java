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
public class LikeHandler implements EventHandler {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private NewsService newsService;

    @Override
    public void doHandler(EventModel eventModel) {
//        System.out.println("LikeHandler doSomething");
//        System.out.println(ToStringBuilder.reflectionToString(eventModel));
        newsService.updateLikeCount(eventModel.getEntityId(),
                Integer.valueOf(eventModel.getExt("likeCount")));


        int toId = eventModel.getToId();
        int fromId = eventModel.getFromId();

        Message msg = new Message();
        msg.setCreatedDate(new Date());
        msg.setToId(toId);
        msg.setFromId(fromId);
        msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) :
                String.format("%d_%d", toId, fromId));

        User user = userService.getUser(fromId);
        News news = newsService.getById(eventModel.getEntityId());
        msg.setContent("用户" + user.getName() + " 赞了你的文章《" + news.getTitle() +
                "》 <a href='http://localhost:8080/news/" + eventModel.getEntityId() + "'>点击前往<a>");
        messageService.addMessage(msg);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
