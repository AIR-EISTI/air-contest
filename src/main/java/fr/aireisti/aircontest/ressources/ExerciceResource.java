package fr.aireisti.aircontest.ressources;

import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.Exercice;
import fr.aireisti.aircontest.models.State;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Calendar;

@Path("/exercice")
public class ExerciceResource {

    private Session session;

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Exercice getExerciceById(@PathParam("id") String id){
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("SELECT e FROM  Exercice e WHERE id=:id");
        Exercice exercice = new Exercice();
        return exercice;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Exercice postExercice(Exercice exercice){

        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(exercice);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return exercice;
    }
}
