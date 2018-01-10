package fr.aireisti.aircontest.models;

import fr.aireisti.aircontest.ressources.InitModel;

import javax.persistence.*;

@Entity
@Table(name = "RESULT", catalog = "aircontest")
public class Result implements InitModel{
    private int id;
    private int point;
    private Exercice exercice;
    private String solution;

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
    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
