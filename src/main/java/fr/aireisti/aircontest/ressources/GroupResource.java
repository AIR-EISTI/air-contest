package fr.aireisti.aircontest.ressources;

import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.Group;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/group")
public class GroupResource {
    private Session session;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Group> getGroup(@DefaultValue("") @QueryParam("search") String search){
        session = HibernateUtil.getSessionFactory().openSession();
        String sql = "SELECT g FROM Group g ";
        if (search != null){
            sql += "WHERE g.name LIKE :search";
        }
        Query query = session.createQuery(sql);
        query.setParameter("search", '%' + search + '%');
        List<Group> groups = query.list();
        session.close();
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
        session.close();
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

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void putGroupById(Group group, @PathParam("id") Integer id) {
        Serializable.updateObject(group, id);
    }

    @GET
    @Path("quantity")
    @Produces(MediaType.APPLICATION_JSON)
    public String getQuantity(){
        session = HibernateUtil.getSessionFactory().openSession();
        String sql = "SELECT COUNT(g) FROM Group g ";
        Query query = session.createQuery(sql);
        String quantity = query.uniqueResult().toString();
        session.close();
        return "{\"quantity\":" + quantity + "}";
    }
}
