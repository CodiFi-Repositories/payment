package in.codifi.ambalal.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tbl_admin_control")
@Data
public class TblAdminControlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private Boolean backoffice;
    private Boolean rms;
    private Boolean globe;
    private Boolean automatic;
    private Boolean manual;
}
