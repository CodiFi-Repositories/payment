package in.codifi.ambalal.controller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import in.codifi.ambalal.controller.spec.ILogController;
import in.codifi.ambalal.model.MailRequestModel;
import in.codifi.ambalal.model.ResponseModel;
import in.codifi.ambalal.model.TechLoginResponse;
import in.codifi.ambalal.service.spec.ILogService;
import in.codifi.api.utilities.CommonMail;

@Path("/logs")
public class LogController implements ILogController {
	@Inject
	ILogService logService;

	@Inject
	CommonMail commonMail;
	
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
	
	/*
	 * method to create a Error Log table 
	
	 */
//	 @Override
//	    public String sendMail(Map<String, Object> request) {
//	        List<String> mailIds = (List<String>) request.get("mailIds");
//	        String subject = (String) request.get("subject");
//	        String message = (String) request.get("message");
//
//	        return logService.sendMails(mailIds, subject, message);
//	    }
	
	 @Override
	    public ResponseModel sendMail(MailRequestModel request) {
	        ResponseModel response = new ResponseModel();
	        String result = commonMail.sendMail(request.getMailIds(), request.getSubject(), request.getMessage());

	        if ("Success".equalsIgnoreCase(result)) {
	            response.setResult("Success");
	            response.setMessage("Mail sent successfully.");
	        } else {
	            response.setResult("Failed");
	            response.setMessage("Mail sending failed.");
	        }

	        return response;
	    }


}