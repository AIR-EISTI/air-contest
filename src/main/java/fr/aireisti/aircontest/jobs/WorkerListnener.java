package fr.aireisti.aircontest.jobs;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import fr.aireisti.aircontest.ressources.JobRessource;
import fr.aireisti.aircontest.worker.ReplyHandler;
import fr.aireisti.aircontest.worker.lib.RunnerResult;

import java.util.Observable;
import java.util.Observer;

public class WorkerListnener implements ServletContextListener, Observer {
    private ReplyHandler replyHandler;

    public void contextInitialized(ServletContextEvent sce) {
        replyHandler = new ReplyHandler();
        ((Observable) replyHandler).addObserver(this);
        replyHandler.start();
    }

    public void contextDestroyed(ServletContextEvent sce){
        replyHandler.stop();
    }

    public void update(Observable observable, Object o) {
        RunnerResult runnerResult = (RunnerResult) o;
        JobRessource.broadcast(runnerResult);
    }
}
