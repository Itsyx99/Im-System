package com.itsyx.message.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: syx
 * @description:
 **/
@Configuration
public class BeanConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 添加InsertBatchSomeColumn方法
     * @return
     */
    @Bean
    public EasySqlInjector easySqlInjector () {
        return new EasySqlInjector();
    }

}
