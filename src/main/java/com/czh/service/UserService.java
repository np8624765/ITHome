package com.czh.service;

import com.czh.dao.LoginTicketDAO;
import com.czh.dao.UserDAO;
import com.czh.entity.LoginTicket;
import com.czh.entity.User;
import com.czh.util.ITHomeUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Chen on 2017/4/18.
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    private final static Random random = new Random();

    public Map<String, Object> regUser(String username, String password){
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgwd","密码不能为空");
            return map;
        }
        if(userDAO.selectByName(username)!=null) {
            map.put("msgname","用户名已经被注册");
            return map;
        }

        User user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
        user.setPassword(ITHomeUtil.MD5(password+user.getSalt()));
        int res = userDAO.addUser(user);
        if(res==0) map.put("msgname","注册失败");
        //Mybatis <setting name="useGeneratedKeys" value="true"/> 在insert成功后自动设置对象主键
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public Map<String, Object> loginUser(String username, String password){
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgwd","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
//        System.out.println("userID");
        if(user==null){
            map.put("msgname","用户名错误");
            return map;
        }
        if(!user.getPassword().equals(ITHomeUtil.MD5(password+user.getSalt()))) {
            map.put("msgpwd","密码错误");
            return map;
        }
        dropLoginTicketByUserId(user.getId());
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    private void dropLoginTicketByUserId(int userId) {
        loginTicketDAO.updateStatusByUserId(userId, 1);
    }

    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatusByTicket(ticket, 1);
    }
}
