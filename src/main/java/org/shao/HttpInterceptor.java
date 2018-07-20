package org.shao;

import lombok.extern.slf4j.Slf4j;
import org.shao.juc.threadlocal.RequestHolder;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        RequestHolder.remove();
        log.info("afterCompletion");
    }
}
