package org.shao;

import lombok.extern.slf4j.Slf4j;
import org.shao.juc.threadlocal.RequestHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@Slf4j
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init, {}", Thread.currentThread().getId());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        log.info("doFilter, {}, {}", Thread.currentThread().getId(), request.getServletPath());
        RequestHolder.add(Thread.currentThread().getId());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        log.info("destroy, {}", Thread.currentThread().getId());
    }

}
