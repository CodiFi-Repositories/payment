package in.codifi.ambalal.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import in.codifi.ambalal.entity.AdminLoginEntity;
import in.codifi.ambalal.repository.AdminLoginRepository;
import in.codifi.ambalal.model.AdminUserCreateRequest;
//import in.codifi.ambalal.repository.ApplicationUserRepository;
//import in.codifi.ambalal.repository.EmployeeRepository;
import in.codifi.ambalal.model.ResponseModel;
import in.codifi.ambalal.service.spec.BaseAdminLoginService;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.api.utilities.EkycEndpointConstants;
import in.codifi.ambalal.error.utility.*;

@ApplicationScoped

public class CustomAdminLoginService implements BaseAdminLoginService {


	@Inject
	ErrorHandling  errorHandling;
	
	@Inject
	CommonMethods commonMethods;
	@Inject
	AdminLoginRepository adminLoginRepository;

	//@Inject
	//ApplicationUserRepository repository;

	private static final Logger logger = LogManager.getLogger(CustomAdminLoginService.class);

	@Override
	public ResponseModel validateLogin(String emailId, String passWord) {
	    ResponseModel responseModel = new ResponseModel();
	    try {
	        if (emailId == null || passWord == null) {
	            return commonMethods.constructFailedMsg(MessageConstants.LOGIN_CREDENTIALS_NULL);
	        }

	        AdminLoginEntity AdminLoginEntity = adminLoginRepository.findByEmailIdAndPassword(emailId, passWord);
	        if (AdminLoginEntity != null) {
	            if ("Active".equalsIgnoreCase(AdminLoginEntity.getStatus())) {
	                String authToken = commonMethods.generateAuthToken(emailId, passWord);
	                responseModel.setToken(authToken);
	                responseModel.setMessage(EkycConstants.SUCCESS_MSG);
	                responseModel.setStat(EkycConstants.SUCCESS_STATUS);
	                responseModel.setReason(MessageConstants.LOGIN_SUCCESS_MESSAGE);
	                responseModel.setResult(AdminLoginEntity);
	            } else {
	                responseModel = commonMethods.constructFailedMsg(MessageConstants.INVALID_STATUS, ErrorCodeConstants.EC021);
	                responseModel.setMessage(EkycConstants.FAILED_MSG);
	            }
	        } else {
	            responseModel = commonMethods.constructFailedMsg(MessageConstants.NO_RECORD_FOUND, ErrorCodeConstants.EC021);
	            responseModel.setMessage(EkycConstants.LOGIN_FAILED); // Optional: user-friendly label
	        }
	    } catch (Exception e) {
	        responseModel = commonMethods.constructFailedMsg(EkycConstants.ADMIN_LOGIN_EXCEPTION, ErrorCodeConstants.EC021);
	        responseModel.setMessage(EkycConstants.FAILED_MSG); // Optional: override display message
	    

	        // Logging into your error handler
	        errorHandling.handleErrors("",
					EkycEndpointConstants.ADMIN_LOGIN, MessageConstants.MODULE, ErrorCodeConstants.EC021,
					EkycConstants.INTERNAL_ERR, EkycConstants.ADMIN_LOGIN_EXCEPTION, EkycConstants.ADMIN_LOGIN_CLASS, e.getMessage(),
					ErrorMessageConstants.ADMIN_LOGIN_FAILED_MESSAGE);
	    }

	    return responseModel;
	}

	
	@Override
	public ResponseModel createUser(AdminUserCreateRequest request) {
	    ResponseModel response = new ResponseModel();
	    try {
	        AdminLoginEntity entity = new AdminLoginEntity();
	        entity.setEmailId(request.getEmailId());
	        entity.setPassword(request.getPassword());
	        entity.setStatus(request.getStatus());
	        entity.setRole(request.getRole());
	        entity.setCreatedBy(request.getCreatedBy());
	        entity.setUpdatedBy(request.getUpdatedBy());
	        
	        if ("Active".equalsIgnoreCase(request.getStatus())) {
	            entity.setActiveStatus(1);
	        } else {
	            entity.setActiveStatus(0);
	        }
	        
	        adminLoginRepository.save(entity);

	        response.setStat(EkycConstants.SUCCESS_STATUS);
	        response.setMessage(EkycConstants.CREATE_ADMIN_USER);
	        response.setResult(entity);
	        
	    } catch (Exception e) {
	    	response = commonMethods.constructFailedMsg(e.getMessage(), ErrorCodeConstants.EC022);
	    	response.setMessage(EkycConstants.USER_CREATION_FAILED); // Custom display message
	    
	        errorHandling.handleErrors("",
					EkycEndpointConstants.CREATE_ADMIN, MessageConstants.MODULE, ErrorCodeConstants.EC022,
					EkycConstants.INTERNAL_ERR, EkycConstants.USER_CREATION_FAILED, EkycConstants.ADMIN_LOGIN_CLASS, e.getMessage(),
					ErrorMessageConstants.CREATE_ADMIN_FAILED_MESSAGE);
	        
	        
	        
	    }

	    return response; // ✅ return your custom object
	}

}


