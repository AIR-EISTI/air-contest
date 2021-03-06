package fr.aireisti.aircontest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.aireisti.aircontest.ressources.InitModel;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="TAG", catalog = "aircontest")
public class Tag implements InitModel {
    private int id;
    private String tag;
    private String color;
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

    @Column(name = "color", nullable = false, length = 6)
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Column(name = "tag", nullable = false, length = 50)
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    @JsonIgnore
    public Set<Exercice> getExercices() {
        return exercices;
    }

    public void setExercices(Set<Exercice> exercices) {
        this.exercices = exercices;
    }
}
