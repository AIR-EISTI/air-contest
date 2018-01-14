package fr.aireisti.aircontest.ressources;


import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.User;
import org.hibernate.Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.stream.Collectors;

@Path("/user")
public class UserResource {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<User> getUsers() {
        Session hibernateSession;
        hibernateSession = HibernateUtil.getSessionFactory().openSession();
        List<User> users = hibernateSession.createQuery("FROM User").list();
        hibernateSession.close();
        return users;
    }
}
