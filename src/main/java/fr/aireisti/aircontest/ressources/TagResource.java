package fr.aireisti.aircontest.ressources;

import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.Tag;
import fr.aireisti.aircontest.security.Secured;
import org.hibernate.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/tag")
public class TagResource {

    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postTag(Tag tag, @Context SecurityContext securityContext) {
        if ( ! securityContext.isUserInRole("Admin") )
            throw new NotAuthorizedException("");
        
        return Serializable.saveObject(tag);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Tag getTag(@PathParam("id") String id) {
        Integer pk;
        try {
            pk = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new NotFoundException();
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        Tag tag = session.get(Tag.class, pk);
        if (tag == null) {
            throw new NotFoundException();
        }
        session.close();
        return tag;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Tag> getTags() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Tag> tags = session.createQuery("SELECT t FROM Tag t").list();
        session.close();
        return tags;
    }

    @PUT
    @Secured
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void putTag(Tag tag, @PathParam("id") Integer id, @Context SecurityContext securityContext) {
        if ( ! securityContext.isUserInRole("Admin") )
            throw new NotAuthorizedException("");

        Serializable.updateObject(tag, id);
    }

    @DELETE
    @Secured
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTag(@PathParam("id") String id, @Context SecurityContext securityContext) {
        if ( ! securityContext.isUserInRole("Admin") )
            throw new NotAuthorizedException("");

        Integer pk;
        try {
            pk = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new NotFoundException();
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        Tag tag = session.get(Tag.class, pk);

        if (tag == null) {
            throw new NotFoundException();
        }
        session.delete(tag);
        session.close();
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
