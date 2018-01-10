package fr.aireisti.aircontest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.aireisti.aircontest.ressources.InitModel;
import fr.aireisti.aircontest.utils.GroupPkListDeserializer;
import fr.aireisti.aircontest.utils.ResultPkListDeserializer;
import fr.aireisti.aircontest.utils.TagPkListDeserializer;

import javax.persistence.*;
import java.util.Calendar;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "EXERCICE", catalog = "aircontest")
public class Exercice implements InitModel {
    private int id;
    private String title;
    private String description;
    private String inputFile;
    private String outputFile;
    private int points;
    private boolean tournament;
    private Timestamp creatingDate;
    @JsonDeserialize(using = TagPkListDeserializer.class)
    private Set<Tag> tags = new HashSet<Tag>(0);
    @JsonDeserialize(using = GroupPkListDeserializer.class)
    private Set<Group> groups = new HashSet<>(0);
    @JsonDeserialize(using = ResultPkListDeserializer.class)
    private Set<Result> results = new HashSet<>(0);

    public Exercice() {
        this.creatingDate = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "title", nullable = false, length = 200)
    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "description", nullable = false)
    public String getDescription() {
        return description;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    @Column(name = "inputFile", nullable = false)
    public String getInputFile() {
        return inputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    @Column(name = "outputFile", nullable = false)
    public String getOutputFile() {
        return outputFile;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Column(name = "points", nullable = false)
    public int getPoints() {
        return points;
    }

    public void setTournament(boolean tournament) {
        this.tournament = tournament;
    }

    @Column(name = "tournament")
    public boolean isTournament() {
        return tournament;
    }

    public void setCreatingDate(java.sql.Timestamp creatingDate) {
        this.creatingDate = creatingDate;
    }

    @Column(name = "creatingDate")
    public Timestamp getCreatingDate() {
        return creatingDate;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "TAG_EXERCICE", catalog = "aircontest",
            joinColumns = { @JoinColumn(name = "exercice_id", nullable = false, updatable = false)},
            inverseJoinColumns = { @JoinColumn(name = "tag_id", nullable = false, updatable = false)}
    )
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "EXERCICE_GROUP", catalog = "aircontest",
            joinColumns = {@JoinColumn(name = "exercice_id", nullable = false, updatable = false)},
            inverseJoinColumns = { @JoinColumn(name = "group_id", nullable = false, updatable = false)}
    )
    public Set<Group> getGroups(){
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "exercice")
    @JsonIgnore
    public Set<Result> getResults() {
        return results;
    }

    public void setResults(Set<Result> results) {
        this.results = results;
    }
}