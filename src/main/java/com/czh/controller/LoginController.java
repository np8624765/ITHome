package com.czh.controller;

import com.czh.service.UserService;
import com.czh.util.ITHomeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Chen on 2017/4/18.
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    @RequestMapping(value = {"/reg"})
    @ResponseBody
    public String register(Model model,
                           HttpServletResponse response,
                           @RequestParam("username") String name,
                           @RequestParam("password") String password,
                           @RequestParam(value = "rember", defaultValue = "0") int remember) {
        try {
            Map<String, Object> map = userService.regUser(name, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", (String) map.get("ticket"));
                cookie.setPath("/");
                if (remember > 0) cookie.setMaxAge(3600 * 24 * 5);
                response.addCookie(cookie);
                return ITHomeUtil.getJSONString(0, "注册成功");
            } else {
                return ITHomeUtil.getJSONString(1, map);
            }
        } catch (Exception e) {
            logger.error("注册异常:" + e.getMessage());
            return ITHomeUtil.getJSONString(1, "注册异常");
        }
    }

    @RequestMapping(value = {"/login"})
    @ResponseBody
    public String login(Model model,
                        HttpServletResponse response,
                        @RequestParam("username") String name,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rember", defaultValue = "0") int remember) {
        try {
            Map<String, Object> map = userService.loginUser(name, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", (String) map.get("ticket"));
                cookie.setPath("/");
                if (remember > 0) cookie.setMaxAge(3600 * 24 * 5);
                response.addCookie(cookie);
                return ITHomeUtil.getJSONString(0, "登录成功");
            } else {
                return ITHomeUtil.getJSONString(1, map);
            }
        } catch (Exception e) {
            logger.error("登录异常:" + e.getMessage());
            return ITHomeUtil.getJSONString(1, "登录异常");
        }
    }

    @RequestMapping(value = {"/logout"})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
