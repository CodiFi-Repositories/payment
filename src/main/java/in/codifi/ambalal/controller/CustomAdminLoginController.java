package in.codifi.ambalal.controller;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Path;

import in.codifi.ambalal.controller.spec.BaseAdminLoginController;
import in.codifi.ambalal.model.ResponseModel;
import in.codifi.ambalal.model.AdminUserCreateRequest;

import in.codifi.ambalal.service.spec.BaseAdminLoginService;
import in.codifi.ambalal.error.utility.MessageConstants;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.ambalal.error.utility.*;


@Path("/adminLogin")
@ApplicationScoped

public class CustomAdminLoginController implements BaseAdminLoginController {

	@Inject
	BaseAdminLoginService AdminloginService;

	@Inject
	CommonMethods commonMethods;

	@Override
	public ResponseModel loginUser(@NotNull String emailID, @NotNull String password) {
	    ResponseModel responseModel;
	    if (emailID != null && password != null) {
	        responseModel = AdminloginService.validateLogin(emailID, password);
	    } else {
	        responseModel = commonMethods.constructFailedMsg(MessageConstants.LOGIN_CREDENTIALS_NULL);
	    }
	    return responseModel;
	}
	
	 @Override
	    public ResponseModel createAdminUser(AdminUserCreateRequest request) {
	        return AdminloginService.createUser(request);
	    }
	
}
