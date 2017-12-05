package fr.aireisti.aircontest.ressources;

import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.Tag;
import org.hibernate.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/tag")
public class TagResource {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Tag getTag(@PathParam("id") String id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Tag tag = session.get(Tag.class, Integer.parseInt(id));
            if (tag == null) {
                throw new NotFoundException();
            }
            return tag;
        } catch (NumberFormatException e) {
            throw new NotFoundException();
        } finally {
            session.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Tag> getTags() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Tag> tags = session.createQuery("SELECT t FROM Tag t").list();
        session.close();
        return tags;
    }
}
