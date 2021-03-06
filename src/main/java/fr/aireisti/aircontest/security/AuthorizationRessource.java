package fr.aireisti.aircontest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.OAuthTransaction;
import fr.aireisti.aircontest.models.Token;
import fr.aireisti.aircontest.models.User;
import fr.aireisti.aircontest.ressources.Serializable;
import fr.aireisti.aircontest.security.OAuthServiceRessource;
import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow;
import org.glassfish.jersey.client.oauth2.TokenResult;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.math.BigInteger;
import java.net.URI;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Path("oauth")
public class AuthorizationRessource {

    @GET
    @Path("authorize")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorize(
            @QueryParam("code") String code,
            @QueryParam("state") String state,
            @DefaultValue("none")
            @CookieParam("TransactionID") String transactionIDCookie)
    {
        if (transactionIDCookie.equals("none")) {
            // Cookie expired or user disabled cookies
            throw new BadRequestException("Missing cookie");
        }

        OAuthTransaction transaction = OAuthServiceRessource.transaction.get(transactionIDCookie);
        OAuthServiceRessource.transaction.remove(transactionIDCookie);

        final OAuth2CodeGrantFlow flow = transaction.getFlow();

        final TokenResult tokenResult = flow.finish(code, state);


        // Get user info from lpmng
        String jsonResponse = queryUser(flow.getAuthorizedClient());

        Map userInfos = extractUserInfo(jsonResponse);
        User user = getUserOrCreate(userInfos);

        Token token = new Token();
        token.setAccessToken(tokenResult.getAccessToken());
        Random random = new SecureRandom();
        token.setTokenContest(new BigInteger(250, random).toString(32));
        token.setUser(user);
        Serializable.saveObject(token);

        URI redirectUri = UriBuilder.fromUri(transaction.getRedirectClientURI())
                .queryParam("tokenContest", token.getTokenContest())
                .queryParam("username", user.getUsername())
                .queryParam("firstname", user.getFirstname())
                .queryParam("surname", user.getSurname())
                .build();
        return Response.seeOther(redirectUri).build();
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
            session.close();
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
