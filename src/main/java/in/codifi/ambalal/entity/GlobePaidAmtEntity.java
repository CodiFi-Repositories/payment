package in.codifi.ambalal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_Glb_Amt_Allocation")
public class GlobePaidAmtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`current_date`")
    private String currentDate;

    @Column(name = "segment")
    private String segment;

    @Column(name = "cmcode")
    private String cmcode;

    @Column(name = "tmcode")
    private String tmcode;

    @Column(name = "cpcode")
    private String cpcode;

    @Column(name = "clicode")
    private String clicode;

    @Column(name = "accounttype")
    private String accountType;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "filler1")
    private String filler1;

    @Column(name = "filler2")
    private String filler2;

    @Column(name = "filler3")
    private String filler3;

    @Column(name = "filler4")
    private String filler4;

    @Column(name = "filler5")
    private String filler5;

    @Column(name = "filler6")
    private String filler6;

    @Column(name = "action")
    private String action;
}

