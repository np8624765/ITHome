package com.czh.configuration;

import com.czh.interceptor.LoginRequiredInterceptor;
import com.czh.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Chen on 2017/4/21.
 */
@Component
public class ITHomeWebConfiguration extends WebMvcConfigurerAdapter{
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/addNews*");
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/uploadImage*");
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:///E:/javaweb/ITHome/upload/");
//        registry.addResourceHandler("/upload/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/upload/");
        super.addResourceHandlers(registry);
    }
}
