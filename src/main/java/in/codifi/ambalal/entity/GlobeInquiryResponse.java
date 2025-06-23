package in.codifi.ambalal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tbl_globe_inquiry_response")
public class GlobeInquiryResponse implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "msg_id", length = 20)
    private String msgId;

    @Column(name = "cur_date")
    @Temporal(TemporalType.DATE)
    private Date curDate;

    @Column(name = "segment")
    private String segment;

    @Column(name = "cm_code")
    private String cmCode;

    @Column(name = "tm_code")
    private String tmCode;

    @Column(name = "cp_code")
    private String cpCode;

    @Column(name = "cli_code")
    private String cliCode;

    @Column(name = "acc_type")
    private String accType;

    @Column(name = "amount")
    private String amt;

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

    @Column(name = "error_code")
    private String errCd;

    @Column(name = "status")
    private String status;

    @Column(name = "message_code")
    private String messagesCode;

    @Column(name = "message")
    private String messages;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();
}
