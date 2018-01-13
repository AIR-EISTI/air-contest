package fr.aireisti.aircontest.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.aireisti.aircontest.ressources.InitModel;

import javax.persistence.*;

@Entity
@Table(name="TOKEN", catalog = "aircontest")
public class Token implements InitModel{
    private String tokenContest;
    private String accessToken;
    private User user;
    //TODO: add creationDate


    public Token() {}

    @Id
    @Column(name = "token", unique = true, nullable = false)
    public String getTokenContest() {
        return tokenContest;
    }
    public void setTokenContest(String token) {
        this.tokenContest = token;
    }

    @JsonIgnore
    @Column(name = "accessToken", unique = true, nullable = false)
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String token) { this.accessToken = token; }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    //Workaround to use class serializable
    public void setId(int it) {}
    @JsonIgnore
    @Transient
    public int getId() {return 4;}
}
