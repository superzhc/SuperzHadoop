package com.github.superzhc.demo.thread;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 2020年07月20日 superz add
 */
public class CyclePrintThread implements Runnable
{
    private static final int DEFAULT_PERIOD = 1;

    int period;

    public CyclePrintThread() {
        this.period = DEFAULT_PERIOD;
    }

    public CyclePrintThread(int period) {
        this.period = period;
    }

    @Override
    public void run() {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (true) {
            System.out.printf("%s[%s]执行开始\n",df.format(new Date()),Thread.currentThread().getName());

            try {
                Thread.sleep(1000*period);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.printf("%s[%s]执行完成\n",df.format(new Date()),Thread.currentThread().getName());
        }
    }
}
