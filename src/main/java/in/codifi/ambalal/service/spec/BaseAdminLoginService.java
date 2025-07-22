package in.codifi.ambalal.service.spec;

import in.codifi.ambalal.model.AdminUserCreateRequest;
import in.codifi.ambalal.model.ResponseModel;

public interface BaseAdminLoginService {

	ResponseModel validateLogin(String emailId, String passWord);

    ResponseModel createUser(AdminUserCreateRequest request);

	

}
