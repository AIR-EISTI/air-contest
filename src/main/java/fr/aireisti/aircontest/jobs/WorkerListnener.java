package fr.aireisti.aircontest.jobs;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import fr.aireisti.aircontest.worker.ReplyHandler;

public class WorkerListnener implements ServletContextListener {
    private ReplyHandler replyHandler;

    public void contextInitialized(ServletContextEvent sce) {
        replyHandler = new ReplyHandler();
        replyHandler.start();
    }

    public void contextDestroyed(ServletContextEvent sce){
        replyHandler.stop();
    }
}
