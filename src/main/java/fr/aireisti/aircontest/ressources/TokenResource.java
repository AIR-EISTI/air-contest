package fr.aireisti.aircontest.ressources;

import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.OAuthTransaction;
import fr.aireisti.aircontest.models.Token;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow;
import org.hibernate.Session;

import javax.management.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.UUID;

@Path("/token")
public class TokenResource {
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@QueryParam("redirectClientURI") String redirectClientURI) {
        OAuthTransaction transaction = new OAuthTransaction();
        transaction.setRedirectClientURI(redirectClientURI);

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
        transaction.setFlow(flow);

        final String transactionID = UUID.randomUUID().toString();
        OAuthServiceRessource.transaction.put(transactionID, transaction);

        final String lpmngAuthURI = flow.start();

        Cookie cookieTransaction = new Cookie("TransactionID", transactionID, "/api/", "");

        return Response.seeOther(UriBuilder.fromUri(lpmngAuthURI).build())
                .cookie(new NewCookie(cookieTransaction, "C'est génial", 3600, false))
                .build();
    }
}
