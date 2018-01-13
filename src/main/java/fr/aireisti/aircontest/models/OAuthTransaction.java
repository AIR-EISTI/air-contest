package fr.aireisti.aircontest.models;

import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow;

public class OAuthTransaction {
    private OAuth2CodeGrantFlow flow;
    private String redirectClientURI;

    public OAuthTransaction() {
    }

    public OAuthTransaction(OAuth2CodeGrantFlow flow, String redirectClientURI) {
        this.flow = flow;
        this.redirectClientURI = redirectClientURI;
    }

    public OAuth2CodeGrantFlow getFlow() {
        return flow;
    }

    public void setFlow(OAuth2CodeGrantFlow flow) {
        this.flow = flow;
    }

    public String getRedirectClientURI() {
        return redirectClientURI;
    }

    public void setRedirectClientURI(String redirectClientURI) {
        this.redirectClientURI = redirectClientURI;
    }
}
