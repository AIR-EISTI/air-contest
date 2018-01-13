package fr.aireisti.aircontest.models;

import fr.aireisti.aircontest.ressources.InitModel;

import javax.persistence.*;

@Entity
@Table(name = "UPLOAD", catalog = "aircontest")
public class Upload implements InitModel{
    private int id;
    private String filename;

    @Column(name = "filename", nullable = false, length = 50)
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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
}
