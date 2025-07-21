package in.codifi.ambalal.model;

import lombok.Data;

@Data
public class TblAdminControlRequest {
    private String userId;
    private boolean backoffice;
    private boolean rms;
    private boolean globe;
    private boolean automatic;
    private boolean manual;
}
