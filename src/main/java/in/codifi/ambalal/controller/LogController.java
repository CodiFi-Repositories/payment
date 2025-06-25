package in.codifi.ambalal.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import in.codifi.ambalal.controller.spec.ILogController;
import in.codifi.ambalal.model.ResponseModel;
import in.codifi.ambalal.service.spec.ILogService;

@Path("/logs")
public class LogController implements ILogController {
	@Inject
	ILogService logService;
	
	/*
	 * method to check the rest access log table if exist or not
	 */
	@Override
	public ResponseModel CreateLogTable() {
		ResponseModel responseModel = new ResponseModel();
		responseModel=logService.checkRestAccessLogTable();
		return responseModel;
	}

	/*
	 * method to check the rest service access log table if exist or not
	 */
	@Override
	public ResponseModel CreateRestLogTable() {
		ResponseModel responseModel = new ResponseModel();
		responseModel=logService.checkRestServiceAccessLogTable();
		return responseModel;
	}
	
	/*
	 * method to create a Error Log table 
	
	 */

	@Override
	public ResponseModel createErrorLogsTable() {
		ResponseModel responseModel = new ResponseModel();
		responseModel=logService.createErrorLogsTable();
		return responseModel;
	}
	
}
