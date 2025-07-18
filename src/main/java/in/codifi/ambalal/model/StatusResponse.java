package in.codifi.ambalal.model;

import lombok.Data;

@Data
public class StatusResponse {
    private String date;
   // private String name;
    private String accountNo;
    private String bankName;
    private Double amount;
    private String status;
}
