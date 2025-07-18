package in.codifi.ambalal.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserCreateRequest {
    private String emailId;
    private String password;
    private String status;
    private String role;
    private String createdBy;
    private String updatedBy;
}
