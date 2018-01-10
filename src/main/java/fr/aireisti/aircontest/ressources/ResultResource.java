package fr.aireisti.aircontest.ressources;

import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.Result;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/result")
public class ResultResource {
    private Session session;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postResult(Result result){
        return Serializable.saveObject(result);
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