package org.shao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Created by hmh on 2018/8/2.
 * @author hmh
 * @Configuration 标注一个类为配置类
 */
@Configuration
@Slf4j
public class ServletConfig {

    // 1.Register ServletContextListener
    @Bean
    public ServletListenerRegistrationBean listenerRegistrationBean() {
        log.info("listenerRegistrationBean...");
        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean();
        bean.setListener(new MyServletContextListener());
        return bean;
    }

    // 2.Register Filter
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        log.info("filterRegistrationBean...");
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new MyFilter());
        bean.setUrlPatterns(Arrays.asList("/*"));

//        bean.addServletRegistrationBeans(new ServletRegistrationBean[]{
//                servletRegistrationBean()
//        });
        return bean;
    }

    // 3.Register Servlet
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        log.info("servletRegistrationBean...");
        ServletRegistrationBean bean = new ServletRegistrationBean();
        bean.setServlet(new MyHttpServlet());
        bean.setUrlMappings(Arrays.asList("/myHttpServlet"));
        return bean;
    }

}
