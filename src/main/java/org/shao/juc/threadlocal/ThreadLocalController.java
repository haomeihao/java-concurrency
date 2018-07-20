package org.shao.juc.threadlocal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@RestController
@RequestMapping("/threadLocal")
public class ThreadLocalController {

    @GetMapping("/test")
    public Long test(){
        return RequestHolder.getId();
    }
}
