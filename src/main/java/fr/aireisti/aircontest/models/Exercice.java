package fr.aireisti.aircontest.models;

import javax.persistence.*;
import java.util.Calendar;
import java.sql.Timestamp;

@Entity
@Table(name = "EXERCICE", catalog = "aircontest")
public class Exercice {
    private int id;
    private String title;
    private String description;
    private String inputFile;
    private String outputFile;
    private State state;
    private int points;
    private boolean tournament;
    private Timestamp creatingDate;

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

}