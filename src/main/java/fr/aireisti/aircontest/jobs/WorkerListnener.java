package fr.aireisti.aircontest.jobs;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.Job;
import fr.aireisti.aircontest.models.JobInfo;
import fr.aireisti.aircontest.models.Result;
import fr.aireisti.aircontest.ressources.JobRessource;
import fr.aireisti.aircontest.ressources.Serializable;
import fr.aireisti.aircontest.worker.ReplyHandler;
import fr.aireisti.aircontest.worker.lib.RunnerResult;
import org.hibernate.Session;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

public class WorkerListnener implements ServletContextListener, Observer {
    private ReplyHandler replyHandler;
    private Session session;

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
        JobInfo jobInfo = new JobInfo();
        if (runnerResult.getStatus() == RunnerResult.SUCCESS) {
            session = HibernateUtil.getSessionFactory().openSession();
            Job job = (Job) session
                    .createQuery("select j from Job j where uuid = :uuid")
                    .setParameter("uuid", runnerResult.getJobId())
                    .uniqueResult();
            session.close();
            Result result = new Result();
            result.setExercice(job.getExercice());
            result.setOutput(runnerResult.getStdout());
            result.computeAccuracy();
            Serializable.saveObject(result);
            if (result.getPoint() == 100) {
                jobInfo.setMsgType(JobInfo.TYPE_INFO);
                jobInfo.setMsgInfo("Exerice réussi !");
            } else {
                jobInfo.setMsgType(JobInfo.TYPE_ERROR);
                jobInfo.setMsgInfo("Solution érronée...");
            }
        } else {
            jobInfo.setMsgType(JobInfo.TYPE_ERROR);
            jobInfo.setMsgInfo("L'execution du code à échouée avec le code d'erreur" + runnerResult.getStatus() + "(" + runnerResult.getError() + ")");
        }
        jobInfo.setUuid(runnerResult.getJobId());
        Timestamp current = new Timestamp(Calendar.getInstance().getTime().getTime());
        jobInfo.setTimestamp(current);
        Serializable.saveObject(jobInfo);
        JobRessource.broadcast(jobInfo);
    }
}
