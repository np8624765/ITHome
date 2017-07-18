package com.czh;

import com.czh.dao.CommentDAO;
import com.czh.dao.NewsDAO;
import com.czh.dao.UserDAO;
import com.czh.entity.Comment;
import com.czh.entity.News;
import com.czh.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ItHomeApplication.class)
public class initDBTests {
    @Autowired
    UserDAO userDAO;

    @Autowired
    NewsDAO newsDAO;

    @Autowired
    CommentDAO commentDAO;

    @Test
    public void addUserTest() {
        Random random = new Random();
        for (int i = 20; i < 35; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);

            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i+1);
            news.setUserId(i+1);
            news.setTitle(String.format("Title-%d", i));
            news.setLink(String.format("http://www.qq.com/%d.html", i));
            newsDAO.addNews(news);
        }
    }

    @Test
    public void selectUserTest() {
        User user = userDAO.selectById(3);
//        System.out.println(user.getId() + ":" + user.getName() + ":" + user.getHeadUrl());
    }

    @Test
    public void updatePwdTest() {
        User user = new User();
        user.setId(1);
        user.setPassword("111");
        int res = userDAO.updatePwdbyId(user);
        System.out.println("-----------" + res);
    }

    @Test
    public void deleteUserTest() {
        int res = userDAO.deleteUserbyId(15);
        System.out.println("-----------" + res);
    }

    @Test
    public void addComment(){
        Comment comment  = new Comment();
        comment.setUserId(2);
        comment.setContent("哈哈哈哈2");
        comment.setCreatedDate(new Date());
        comment.setEntityId(1);
        comment.setEntityType(1);
        comment.setStatus(0);
        assert(commentDAO.addComment(comment)==1);
    }

    @Test
    public void getCommentCount(){
        System.out.println(commentDAO.getCommentCount(1,1));
    }

    @Test
    public void selectComment(){
        List<Comment> comments = commentDAO.selectByEntity(1, 1);
        for (Comment c : comments) {
            System.out.println(c);
        }
    }
}
