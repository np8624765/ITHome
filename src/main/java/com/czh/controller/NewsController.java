package com.czh.controller;

import com.czh.asyn.EventModel;
import com.czh.asyn.EventProducer;
import com.czh.asyn.EventType;
import com.czh.entity.*;
import com.czh.service.CommentService;
import com.czh.service.LikeService;
import com.czh.service.NewsService;
import com.czh.service.UserService;
import com.czh.util.ITHomeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.channels.ClosedChannelException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Chen on 2017/4/24.
 */
@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(value = ("/uploadImage"), method = RequestMethod.POST)
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = newsService.saveImage(file);
            if (fileUrl == null) return ITHomeUtil.getJSONString(1, "上传失败");
            return ITHomeUtil.getJSONString(0, fileUrl);
        } catch (Exception ex) {
            logger.error("上传失败:" + ex.getMessage());
            return ITHomeUtil.getJSONString(1, "上传失败");
        }
    }

    @RequestMapping(value = ("/addNews"),  method = RequestMethod.POST)
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            News news = new News();
            if (hostHolder.getUser() == null) return ITHomeUtil.getJSONString(1, "添加资讯错误，未登录！");
            news.setUserId(hostHolder.getUser().getId());
            news.setCreatedDate(new Date());
            news.setImage(image);
            news.setTitle(title);
            news.setLink(link);
            int res = newsService.addNews(news);
            if (res == 0) return ITHomeUtil.getJSONString(1, "添加资讯错误");
            return ITHomeUtil.getJSONString(0);
        } catch (Exception ex) {
            logger.error("添加资讯错误:" + ex.getMessage());
            return ITHomeUtil.getJSONString(1, "添加资讯错误");
        }
    }

    @RequestMapping(value = ("/news/{newsId}"), method = RequestMethod.GET)
    public String showNewsDetail(@PathVariable(value = "newsId") int newsId, Model model) {
        News news = newsService.getById(newsId);
        if (news == null) return "error";
        User user = userService.getUser(news.getUserId());
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        if (localUserId != 0) {
            model.addAttribute("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
        } else {
            model.addAttribute("like", 0);
        }
        List<Comment> cs = commentService.getCommentByEntity(newsId, EntityType.ENTITY_NEWS);
        List<ViewObject> comments = new ArrayList<>();
        for (Comment c : cs) {
            ViewObject v = new ViewObject();
            v.set("comment", c);
            v.set("user", userService.getUser(c.getUserId()));
            comments.add(v);
        }

        model.addAttribute("news", news);
        model.addAttribute("owner", user);
        model.addAttribute("comments", comments);
        return "detail";
    }

    @RequestMapping(value = ("/addComment"), method = RequestMethod.POST)
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            if (hostHolder.getUser() == null) return ITHomeUtil.getJSONString(1, "添加评论错误，未登录！");
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setStatus(0);
            commentService.addComment(comment);

            int commentCount = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(newsId, commentCount);

            //异步队列
            EventModel eventModel = new EventModel();
            eventModel.setEventType(EventType.COMMENT)
                    .setEntityId(newsId)
                    .setEntityType(EntityType.ENTITY_COMMENT)
                    .setFromId(comment.getUserId())
                    .setToId(newsService.getById(newsId).getUserId());
            eventProducer.commitEvent(eventModel);


        } catch (Exception ex) {
            logger.error("添加评论错误:" + ex.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }

}
