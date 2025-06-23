package in.codifi.ambalal.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "tbl_rms_update_response")
public class RmsUpdateResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String code;
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // Optional: you can add fields to store request data like clientId, etc.

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

}
