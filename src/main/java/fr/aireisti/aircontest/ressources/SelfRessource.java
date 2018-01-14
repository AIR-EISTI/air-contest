package fr.aireisti.aircontest.ressources;

import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.User;
import fr.aireisti.aircontest.security.Secured;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("/me")
public class SelfRessource {
    @GET
    @Secured
    @Produces({MediaType.APPLICATION_JSON})
    public User getUser(@Context SecurityContext securityContext) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("SELECT u FROM User u WHERE u.username = :username");
        query.setParameter("username", securityContext.getUserPrincipal().getName());
        User user = (User) query.uniqueResult();

        return user;
    }
}
