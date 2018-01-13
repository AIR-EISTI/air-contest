package fr.aireisti.aircontest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.aireisti.aircontest.ressources.InitModel;
import fr.aireisti.aircontest.utils.ExercicePkDeserializer;
import org.apache.commons.text.similarity.JaroWinklerDistance;

import javax.persistence.*;

@Entity
@Table(name = "RESULT", catalog = "aircontest")
public class Result implements InitModel{
    private int id;
    private int point;
    @JsonDeserialize(using = ExercicePkDeserializer.class)
    private Exercice exercice;
    private String code;
    private String output;

    public Result() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "point", nullable = false)
    public int getPoint(){
        return point;
    }

    public void setPoint(int points) {
        this.point = points;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="exercice_id", nullable = false)
    public Exercice getExercice(){
        return exercice;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    @Transient
    @JsonIgnore
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Transient
    @JsonIgnore
    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void computeAccuracy() {
        JaroWinklerDistance distance = new JaroWinklerDistance();
        setPoint((int) Math.floor(distance.apply(output, getExercice().getOutputFile())*100));
    }
}
