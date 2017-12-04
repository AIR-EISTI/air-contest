package fr.aireisti.aircontest.ressources;

import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.Group;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/group")
public class GroupResource {
    private Session session;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Group> getGroup(){
        session = HibernateUtil.getSessionFactory().openSession();
        List<Group> groups = session.createQuery("SELECT g FROM Group g").list();
        return groups;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postGroup(Group group){
        return Serializable.saveObject(group);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Group getGroupId(@PathParam("id") Integer id){
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("SELECT g FROM Group g WHERE id=:id");
        query.setParameter("id", id);
        Group group = (Group) query.uniqueResult();
        if (group == null){
            throw new NotFoundException();
        }
        return group;
    }

    @DELETE
    @Path("{id}")
    public void deleteGroupById(@PathParam("id") Integer id){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("DELETE FROM Group g WHERE g.id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
        tx.commit();
        session.close();
    }
}
