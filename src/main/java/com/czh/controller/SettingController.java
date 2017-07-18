package com.czh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Chen on 2017/4/17.
 */
@Controller
public class SettingController {
    @RequestMapping(value = {"/setting"})
    @ResponseBody
    public String setting() {
        return "Setting!";
    }
}
