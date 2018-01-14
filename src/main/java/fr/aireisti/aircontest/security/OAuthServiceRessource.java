package fr.aireisti.aircontest.security;

import fr.aireisti.aircontest.models.OAuthTransaction;
import org.glassfish.jersey.client.oauth2.ClientIdentifier;

import java.util.HashMap;
import java.util.Map;

public class OAuthServiceRessource {
    private static ClientIdentifier clientIdentifier = new ClientIdentifier(
            "ETvzK6rZ9T8h6vKcbK4giVQHWYIWqeIaWvIXLPF6",
            "gM8mfZhqX4LvlXnRXsEUTCdGDvP8j7dKR0tqQFlTMYj8b6Yp3LsjVBSPeaApMNXni05PIIf6CzdL8PVFmwYgq0NwUbgZNPgdnZGvpsFwnGWZOQQA1sOAdfHPWOqKkF6m");
    public static Map<String, OAuthTransaction> transaction = new HashMap<String, OAuthTransaction>();

    public static ClientIdentifier getClientIdentifier() {
        return clientIdentifier;
    }
}
