package com.czh;

import com.czh.entity.User;
import com.czh.util.JedisAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Chen on 2017/5/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ItHomeApplication.class)
@WebAppConfiguration
public class JedisAdapterTests {
    @Autowired
    JedisAdapter jedisAdapter;

    @Test
    public void setAndGetObjTest() {
        User user = new User();
        user.setId(1);
        user.setPassword("sfsfsefesf");
        user.setSalt("salt");
        user.setHeadUrl("www.qq.com");
        user.setName("张三");
        jedisAdapter.setObject("obj", user);


        User t = jedisAdapter.getObject(User.class, "obj");
        System.out.println(ToStringBuilder.reflectionToString(t));
    }
}
