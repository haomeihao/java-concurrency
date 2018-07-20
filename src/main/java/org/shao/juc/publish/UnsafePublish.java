package org.shao.juc.publish;

import lombok.extern.slf4j.Slf4j;
import org.shao.annoations.NotThreadSafe;

import java.util.Arrays;

/**
 *
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@Slf4j
@NotThreadSafe
public class UnsafePublish {

    private String[] states = {"a","b","c"};

    public String[] getStates() {
        return states;
    }

    public static void main(String[] args) {
        UnsafePublish unsafePublish = new UnsafePublish();
        log.info("{}", Arrays.toString(unsafePublish.getStates()));

        unsafePublish.getStates()[0] = "d";
        log.info("{}", Arrays.toString(unsafePublish.getStates()));
    }
}
