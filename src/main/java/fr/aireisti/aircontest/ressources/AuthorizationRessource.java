package fr.aireisti.aircontest.ressources;

import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow;
import org.glassfish.jersey.client.oauth2.TokenResult;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@Path("oauth")
public class AuthorizationRessource {

    @GET
    @Path("authorize")
    public void authorize(
            @Context HttpHeaders headers)
    {
        StringBuilder sb = new StringBuilder();
        for (String name : headers.getCookies().keySet()) {
            sb.append(name + " ");
        }
        System.out.println(sb.toString());
    }
}
