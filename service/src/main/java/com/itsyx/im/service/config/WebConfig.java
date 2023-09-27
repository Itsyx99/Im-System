package com.itsyx.im.service.config;

import com.itsyx.im.service.interceptor.GateWayInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    GateWayInterceptor gateWayInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(gateWayInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/v1/user/login")
                .excludePathPatterns("/v1/message/checkSend")
                .excludePathPatterns("/v1/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }

}
