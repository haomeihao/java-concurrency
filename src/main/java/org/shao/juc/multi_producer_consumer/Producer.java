package org.shao.juc.multi_producer_consumer;

/**
 * 生产者
 * Created by hmh on 2018/7/20.
 * @author hmh
 */
public class Producer implements Runnable {

    private Resource resource;

    public Producer(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource.produce();
        }
    }
}
