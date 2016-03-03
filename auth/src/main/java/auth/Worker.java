package auth;

import auth.starter.AuthStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by Dell on 2016/3/2.
 */
public class Worker extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(Worker.class);

    public static boolean _stop =false;
    public static ConcurrentLinkedDeque<IMHandler> _tasks = new ConcurrentLinkedDeque<>();

    public static void dispatch(IMHandler handler) {
        _tasks.offer(handler);
    }

    @Override
    public void run() {
        while(!_stop) {
            //todo 如何取
            IMHandler handler = _tasks.poll();
            try {
                handler.excute(this);
            } catch (Exception e) {
                logger.error("Caught Exception");
            }
        }
    }

    public static int getWorkId(String str) {
        return str.hashCode() % AuthStarter.workNum;
    }

    public static Worker[] startWorker(int workNum) {
        Worker[] workers = new Worker[workNum];
        for(int i = 0; i < workNum; i++) {
            workers[i].start();
        }
        return workers;
    }
}
