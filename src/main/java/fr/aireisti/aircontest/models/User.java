package fr.aireisti.aircontest.models;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USER", catalog = "aircontest")
public class User {
    private int id;
    private String username;
    private String firstname;
    private String surname;
    private Set<Token> tokenSet = new HashSet<>(0);

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    @Column(name = "username", unique = true, nullable = false)
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    @Column(name = "firstname")
    public String getFirstname() {return firstname;}
    public void setFirstname(String firstname) {this.firstname = firstname;}

    @Column(name = "surname")
    public String getSurname() {return surname;}
    public void setSurname(String surname) {this.surname = surname;}

    @OneToMany(mappedBy = "user")
    public Set<Token> getTokenSet() {return tokenSet;}
    public void setTokenSet(Set<Token> tokenSet) {this.tokenSet = tokenSet;}
}
