package fr.aireisti.aircontest.models;

import fr.aireisti.aircontest.models.Exercice;
import fr.aireisti.aircontest.ressources.InitModel;
import org.ini4j.Ini;

import javax.persistence.*;

@Entity
@Table(name = "JOB", catalog = "aircontest")
public class Job implements InitModel {
    private String uuid;
    private User user;
    private Exercice exercice;

    @Id
    @Column(name = "uuid", unique = true, nullable = false)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
