package fr.aireisti.aircontest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.aireisti.aircontest.ressources.InitModel;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "JOB_INFO", catalog = "aircontest")
public class JobInfo implements InitModel{
    private int id;
    private String uuid;
    private Timestamp timestamp;
    private String msgType;
    private String msgInfo;


    static public final String TYPE_ERROR = "error";
    static public final String TYPE_INFO = "info";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @JsonIgnore
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "execTime")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Column(name = "msgType")
    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Column(name = "msgInfo")
    public String getMsgInfo() {
        return msgInfo;
    }

    public void setMsgInfo(String msgInfo) {
        this.msgInfo = msgInfo;
    }

    @Column(name = "uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
