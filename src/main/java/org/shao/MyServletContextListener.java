package org.shao;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by hmh on 2018/8/2.
 * @author hmh
 */
@Slf4j
public class MyServletContextListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent event) {
        log.info("contextInitialized...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        log.info("contextDestroyed...");
    }

}
