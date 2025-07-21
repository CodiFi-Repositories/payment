package in.codifi.ambalal.service.spec;

import java.util.List;

import in.codifi.ambalal.model.ResponseModel;

public interface ILogService {

	/**
	 * method to check rest access log table
	 * 
	 * @return
	 */
	ResponseModel checkRestAccessLogTable();
	
	/**
	 * method to check rest service access log table
	 * 
	 * @return
	 */
	ResponseModel checkRestServiceAccessLogTable();
	
	/**
	 * method to check rest service access log table
	 *@author Vennila 
	 * @return
	 */
	ResponseModel createErrorLogsTable();
	
	/**
	 * method to send error mail
	 *@author 
	 * @return
	 */
 //   String sendMails(List<String> mailIds, String subject, String message);

	
	
}
