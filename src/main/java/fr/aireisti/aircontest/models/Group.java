package fr.aireisti.aircontest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.aireisti.aircontest.ressources.InitModel;
import fr.aireisti.aircontest.utils.GroupPkListDeserializer;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "GROUP", catalog = "aircontest")
public class Group implements InitModel {
    private int id;
    private String name;
    private String description;
    private java.sql.Timestamp publicationDate;
    private java.sql.Timestamp endDate;
    private int points;
    private String imageUrl;
    private Set<Exercice> exercices = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "publicationDate", nullable = false)
    public Timestamp getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Timestamp publicationDate) {
        this.publicationDate = publicationDate;
    }

    @Column(name = "endDate", nullable = false)
    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    @Column(name = "points", nullable = false)
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
    @JsonIgnore
    public Set<Exercice> getExercices(){
        return exercices;
    }

    public void setExercices(Set<Exercice> exercices){
        this.exercices = exercices;
    }

    @Column(name = "imageUrl", nullable = false)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
