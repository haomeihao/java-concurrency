package org.shao.juc.publish;

import lombok.extern.slf4j.Slf4j;
import org.shao.annoations.NotRecommend;
import org.shao.annoations.NotThreadSafe;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@Slf4j
@NotThreadSafe
@NotRecommend
public class Escape {

    private int thisCanBeEscape = 0;

    public Escape() {
        new InnerClass();
    }

    private class InnerClass{
        public InnerClass() {
            log.info("{}", Escape.this.thisCanBeEscape);
        }
    }

    public static void main(String[] args) {
        new Escape();
    }
}
