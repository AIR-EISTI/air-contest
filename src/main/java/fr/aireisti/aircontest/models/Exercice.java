package fr.aireisti.aircontest.models;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlRootElement;

import java.sql.DatabaseMetaData;
import java.util.Date;

@XmlRootElement
public class Exercice {
    private int id;
    private String title;
    private String description;
    //private File inputFile;
    //private File outputFile;
    private State state;
    private int points;
    private boolean tournament;
    private java.sql.Timestamp dateStart;
    private java.sql.Timestamp dateEnd;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
/*
    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public File getInputFile() {
        return inputFile;
    }
*/
    public void setState(State state) {
        this.state = state;
    }

    public void setState(String state_str) {
        this.state = State.valueOf(state_str);
    }

    public State getState() {
        return state;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public void setTournament(boolean tournament) {
        this.tournament = tournament;
    }

    public boolean isTournament() {
        return tournament;
    }

    public void setDateStart(java.sql.Timestamp dateStart) {
        this.dateStart = dateStart;
    }

    public java.sql.Timestamp getDateStart() {
        return dateStart;
    }

    public void setDateEnd(java.sql.Timestamp dateEnd) {
        this.dateEnd = dateEnd;
    }

    public java.sql.Timestamp getDateEnd(){
        return dateEnd;
    }

}