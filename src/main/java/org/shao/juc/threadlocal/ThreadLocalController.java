package org.shao.juc.threadlocal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@RestController
@RequestMapping("/threadLocal")
@Slf4j
public class ThreadLocalController {

    @GetMapping("/test")
    public Long test(){
        log.info("/threadLocal/test...");
        return RequestHolder.getId();
    }
}
