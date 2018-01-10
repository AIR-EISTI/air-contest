package fr.aireisti.aircontest.ressources;

import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.Result;
import fr.aireisti.aircontest.worker.Sender;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Path("/result")
public class ResultResource {
    private Session session;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postResult(Result result){
        if (result.getCode() != null) {
            String errorMsg = "{\"error\": \"Failed to create job\"}";
            String uuid;
            try {
                Sender sender = new Sender();
                uuid = sender.call("python", result.getCode(), result.getExercice().getInputFile());
            } catch (IOException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
            } catch (TimeoutException e) {
                return Response.status(Response.Status.GATEWAY_TIMEOUT).entity(errorMsg).build();
            }

            if (uuid == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
            }

            return Response.accepted().entity("{\"jobId\":\"" + uuid + "\"}").build();
        }

        try {
            result.computeAccuracy();
            Serializable.saveObject(result);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Either one of 'code' or 'output' must be not null.\"}").build();
        }

        result.setExercice(ExerciceResource.renderDatasetLink(result.getExercice()));

        return Response.ok().entity(result).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Result> getResult(){
        session = HibernateUtil.getSessionFactory().openSession();
        String sql = "SELECT r FROM Result r";
        Query query = session.createQuery(sql);
        List<Result> results = query.list();
        session.close();
        return results;
    }
}