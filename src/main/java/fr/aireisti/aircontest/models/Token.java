package fr.aireisti.aircontest.models;


import javax.persistence.*;

@Entity
@Table(name="TOKEN", catalog = "aircontest")
public class Token {
    private String token;
    private User user;
    //TODO: add creationDate


    public Token(User user) {
        setToken("plouf");
        setUser(user);
    }

    @Id
    @Column(name = "token", unique = true, nullable = false)
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
