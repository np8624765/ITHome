package com.czh.controller;

import com.czh.entity.EntityType;
import com.czh.entity.HostHolder;
import com.czh.entity.News;
import com.czh.entity.ViewObject;
import com.czh.service.LikeService;
import com.czh.service.NewsService;
import com.czh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen on 2017/4/18.
 */
@Controller
public class HomeController {
    @Autowired
    UserService userService;

    @Autowired
    NewsService newsService;

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    private List<ViewObject> getNews(int userId, int offset, int limit) {
        List<News> newsList = newsService.getLastedNews(userId, offset, limit);
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<ViewObject> vos = new ArrayList<>();
        for (News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUserId()));
            if (localUserId != 0) {
                vo.set("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            } else {
                vo.set("like", 0);
            }
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(value={"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("vos", getNews(0, 0, 10));
        if (hostHolder.getUser() != null) pop = 0;
        model.addAttribute("pop", pop);
        return "home";
    }

    @RequestMapping(value={"/user/{userId}"}, method = RequestMethod.GET)
    public String userIndex(Model model,
                            @PathVariable("userId") int userId,
                            @RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("vos", getNews(userId, 0, 10));
        if (hostHolder.getUser() != null) pop = 0;
        model.addAttribute("pop", pop);
        return "home";
    }
}
