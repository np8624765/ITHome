package com.czh.controller;

import com.czh.asyn.EventModel;
import com.czh.asyn.EventProducer;
import com.czh.asyn.EventType;
import com.czh.entity.EntityType;
import com.czh.entity.HostHolder;
import com.czh.service.LikeService;
import com.czh.service.NewsService;
import com.czh.util.ITHomeUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Chen on 2017/5/8.
 */
@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private NewsService newsService;
    @Autowired
    private EventProducer eventProducer;


    @RequestMapping(value = "/like")
    @ResponseBody
    public String like(@RequestParam(value = "newsId") int newsId) {
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.like(userId, EntityType.ENTITY_NEWS, newsId);

        //异步队列
        EventModel eventModel = new EventModel();
        eventModel.setEventType(EventType.LIKE).setFromId(userId)
                .setToId(newsService.getById(newsId).getUserId()).setEntityId(newsId)
                .setEntityType(EntityType.ENTITY_NEWS).setExt("likeCount", String.valueOf(likeCount));

        eventProducer.commitEvent(eventModel);

        return ITHomeUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(value = "/dislike")
    @ResponseBody
    public String dislike(@RequestParam(value = "newsId") int newsId) {
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.dislike(userId, EntityType.ENTITY_NEWS, newsId);

        //异步队列
        EventModel eventModel = new EventModel();
        eventModel.setEventType(EventType.DISLIKE).setEntityId(newsId)
                .setEntityType(EntityType.ENTITY_NEWS)
                .setExt("likeCount", String.valueOf(likeCount));
        eventProducer.commitEvent(eventModel);

        return ITHomeUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
