package fr.aireisti.aircontest.ressources;

import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.Job;
import fr.aireisti.aircontest.models.Result;
import fr.aireisti.aircontest.models.User;
import fr.aireisti.aircontest.security.Secured;
import fr.aireisti.aircontest.worker.Sender;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Path("/result")
public class ResultResource {
    private Session session;

    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postResult(Result result, @Context SecurityContext securityContext) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("SELECT u FROM User u WHERE u.username = :username");
        query.setParameter("username", securityContext.getUserPrincipal().getName());
        User user = (User) query.uniqueResult();
        session.close();

        result.setUser(user);

        if (result.getCode() != null && result.getCode().trim().length() > 0) {
            String uuid;
            try {
                Sender sender = new Sender();
                uuid = sender.prepare("python", result.getCode(), result.getExercice().getInputFile());
                JobRessource.addBroadcaster(uuid);
                Job job = new Job();
                job.setExercice(result.getExercice());
                job.setUuid(uuid);
                job.setUser(user);
                Serializable.saveObject(job);
                sender.call();
            } catch (IOException e) {
                throw new InternalServerErrorException("Failed to create job");
            } catch (TimeoutException e) {
                throw new javax.ws.rs.ServiceUnavailableException("Failed to create job");
            }

            if (uuid == null) {
                throw new InternalServerErrorException("Failed to create job");
            }

            return Response.accepted().entity("{\"jobId\":\"" + uuid + "\"}").build();
        }

        try {
            result.computeAccuracy();
            Serializable.saveObject(result);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Either one of 'code' or 'output' must be not null.");
        }

        result.setExercice(ExerciceResource.renderDatasetLink(result.getExercice()));

        return Response.ok().entity(result).build();
    }

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<Result> getResult(@Context SecurityContext securityContext){
        session = HibernateUtil.getSessionFactory().openSession();

        Query query = session.createQuery("SELECT u FROM User u WHERE u.username = :username");
        query.setParameter("username", securityContext.getUserPrincipal().getName());
        User user = (User) query.uniqueResult();

        String sql = "SELECT r FROM Result r";

        if (!user.getRole().equals("Admin")) {
            sql += " WHERE user_id = :user_id";
            query = session.createQuery(sql);
            query.setParameter("user_id", user.getId());
        } else {
            query = session.createQuery(sql);
        }
        List<Result> results = query.list();
        session.close();
        return results;
    }

    @GET
    @Path("summary")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getSummary(@Context SecurityContext securityContext) {
        session = HibernateUtil.getSessionFactory().openSession();

        Query query = session.createQuery("SELECT u FROM User u WHERE u.username = :username");
        query.setParameter("username", securityContext.getUserPrincipal().getName());
        User user = (User) query.uniqueResult();

        String hql = "SELECT count(distinct r), sum(r.exercice.points) FROM Result r WHERE user_id = :user_id and point = 100";
        query = session.createQuery(hql);
        query.setParameter("user_id", user.getId());
        Object[] res = (Object[]) query.uniqueResult();
        session.close();
        Map<String, Object> map = new HashMap<>();
        map.put("exoResolus", res[0]);
        map.put("pointExos", res[1]);
        return map;
    }

}