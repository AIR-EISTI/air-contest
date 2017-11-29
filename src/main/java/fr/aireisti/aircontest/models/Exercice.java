package fr.aireisti.aircontest.models;

import sun.util.calendar.BaseCalendar;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@XmlRootElement
public class Exercice {
    private int id;
    private String title;
    private String description;
    private String inputFile;
    private String outputFile;
    private State state;
    private int points;
    private boolean tournament;
    private java.sql.Timestamp creatingDate;

    public Exercice() {
        this.creatingDate = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
    }

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

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

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

    public void setCreatingDate(java.sql.Timestamp creatingDate) {
        this.creatingDate = creatingDate;
    }

    public java.sql.Timestamp getCreatingDate() {
        return creatingDate;
    }

}