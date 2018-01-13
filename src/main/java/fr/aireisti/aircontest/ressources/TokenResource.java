package fr.aireisti.aircontest.ressources;

import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.Token;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow;
import org.hibernate.Session;

import javax.management.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.UUID;

@Path("/path")
public class TokenResource {
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticateUser() {

        try {

            // Authenticate the user using the credentials provided
            return authenticate();

            // Issue a token for the user
            //String token = issueToken(username);

            // Return the token on the response
            //return Response.ok(token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private Response authenticate() throws Exception {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
        final String redirectURI = UriBuilder.fromUri(uriInfo.getBaseUri())
                .path("oauth/authorize/").build().toString();

        final OAuth2CodeGrantFlow flow = OAuth2ClientSupport.
                authorizationCodeGrantFlowBuilder(
                        OAuthServiceRessource.getClientIdentifier(),
                        "http://localhost:8000/o/authorize/",
                        "http://localhost:8000/o/token/")
                .scope("read write")
                .redirectUri(redirectURI)
                .build();
        final String transactionID = UUID.randomUUID().toString();
        OAuthServiceRessource.flows.put(transactionID, flow);

        final String lpmngAuthURI = flow.start();

        Cookie cookieTransaction = new Cookie("TransactionID", transactionID);

        return Response.seeOther(UriBuilder.fromUri(lpmngAuthURI).build())
                .cookie(new NewCookie(cookieTransaction, "C'est génial", 3600, false))
                .build();
    }

    private void issueToken(String username) {
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token

    }

    /*
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public Response getToken() {

        Session hibernateSession;
        hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Token token = new Token(user);
        return token
    }*/
}
