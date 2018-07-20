package org.shao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@RestController
@Slf4j
public class TestController {

    @RequestMapping("/test")
    public String test() {
        return "test";
    }
}
