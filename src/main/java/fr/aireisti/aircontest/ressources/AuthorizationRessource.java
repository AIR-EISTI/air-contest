package fr.aireisti.aircontest.ressources;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.Token;
import fr.aireisti.aircontest.models.User;
import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow;
import org.glassfish.jersey.client.oauth2.TokenResult;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.UUID;

@Path("oauth")
public class AuthorizationRessource {

    @GET
    @Path("authorize")
    @Produces(MediaType.APPLICATION_JSON)
    public Token authorize(
            @QueryParam("code") String code,
            @QueryParam("state") String state,
            @DefaultValue("none")
            @CookieParam("TransactionID") String transactionIDCookie)
    {
        if (transactionIDCookie.equals("none")) {
            // Cookie expired or user disabled cookies
            throw new BadRequestException("Missing cookie");
        }

        final OAuth2CodeGrantFlow flow = OAuthServiceRessource.flows.get(transactionIDCookie);
        OAuthServiceRessource.flows.remove(transactionIDCookie);

        final TokenResult tokenResult = flow.finish(code, state);


        // Get user info from lpmng
        String jsonResponse = queryUser(flow.getAuthorizedClient());

        Map userInfos = extractUserInfo(jsonResponse);
        User user = getUserOrCreate(userInfos);

        Token token = new Token();
        token.setAccessToken(tokenResult.getAccessToken());
        token.setTokenContest(UUID.randomUUID().toString());
        token.setUser(user);
        Serializable.saveObject(token);

        return token;
    }


    private String queryUser(Client client) {
        Response response = client.target("http://localhost:8000/").path("me/")
                .request(MediaType.APPLICATION_JSON).get();

        return response.readEntity(String.class);
    }


    private Map extractUserInfo(String jsonUser) {
        Map user = null;

        try {

            ObjectMapper mapper = new ObjectMapper();
            user = mapper.readValue(jsonUser, Map.class);

        }
        catch (Exception e) {
            System.out.println("oups");
        }

        return user;
    }


    private User getUserOrCreate(Map userInfos) {
        User user = null;

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("SELECT u FROM User u WHERE u.username = :username");
            query.setParameter("username", userInfos.get("username"));

            user = (User)query.uniqueResult();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (user == null) {
            user = new User();
            user.setUsername(userInfos.get("username").toString());
            user.setFirstname(userInfos.get("first_name").toString());
            user.setSurname(userInfos.get("last_name").toString());
            Serializable.saveObject(user);
        }

        return user;
    }
}
