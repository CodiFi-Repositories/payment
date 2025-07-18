package in.codifi.ambalal.service.spec;

import in.codifi.ambalal.model.TblAdminControlRequest;
import in.codifi.ambalal.model.ResponseModel;

import java.util.List;

import in.codifi.ambalal.model.AdminControlRequest;


public interface BaseTblAdminControlService {
	ResponseModel saveTblAdminControls(TblAdminControlRequest request);

	ResponseModel getAdminControlByUserId(String userId);
	
	
    ResponseModel updateControl(List<AdminControlRequest> requestList);


}

