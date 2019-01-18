package org.shao.netty.io;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * Created by hmh on 2019/1/15.
 */
public class IOClient {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8000);
                while (true) {
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write((new Date() + ": hello word").getBytes());
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
