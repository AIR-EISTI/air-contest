package fr.aireisti.aircontest.ressources;


import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.User;
import fr.aireisti.aircontest.security.Secured;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import java.util.List;

@Path("/users")
public class UserResource {

    @GET
    @Secured
    @Produces({MediaType.APPLICATION_JSON})
    public List<User> getUsers(@Context SecurityContext securityContext) {
        if (securityContext.isUserInRole("Admin")) {
            Session hibernateSession;
            hibernateSession = HibernateUtil.getSessionFactory().openSession();
            List<User> users = hibernateSession.createQuery("FROM User").list();
            hibernateSession.close();
            return users;
        }

        throw new NotAuthorizedException("");
    }

    @GET
    @Secured
    @Path("quantity")
    @Produces({MediaType.APPLICATION_JSON})
    public String userQuantity(@Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole("Admin")) {
            throw new NotAuthorizedException("");
        }

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Query query = hibernateSession.createQuery("SELECT COUNT(u) FROM User u");
        String result = query.uniqueResult().toString();
        hibernateSession.close();
        return "{\"quantity\":" + result + "}";
    }
}
