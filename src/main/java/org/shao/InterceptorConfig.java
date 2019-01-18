package org.shao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by hmh on 2018/8/2.
 * @author hmh
 * 配置spring mvc的拦截器 WebMvcConfigurerAdapter
 */
@Configuration
@Slf4j
public class InterceptorConfig extends WebMvcConfigurerAdapter{

    // 4.add Interceptor
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("addInterceptors...");
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**");
    }
}
