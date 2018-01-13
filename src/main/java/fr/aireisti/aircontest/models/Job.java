package fr.aireisti.aircontest.models;

import fr.aireisti.aircontest.models.Exercice;
import fr.aireisti.aircontest.ressources.InitModel;
import org.ini4j.Ini;

import javax.persistence.*;

@Entity
@Table(name = "JOB", catalog = "aircontest")
public class Job implements InitModel {
    private String uuid;
    private int userId;
    private Exercice exercice;

    @Id
    @Column(name = "uuid", unique = true, nullable = false)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "user")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="exercice_id", nullable = false)
    public Exercice getExercice() {
        return exercice;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    //Workaround to use class serializable
    public void setId(int it) {}
    @Transient
    public int getId() {return 4;}
}
