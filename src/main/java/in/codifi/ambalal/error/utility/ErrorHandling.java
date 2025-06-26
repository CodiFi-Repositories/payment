package in.codifi.ambalal.error.utility;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import in.codifi.ambalal.model.ResponseModel;
//import in.codifi.ambalal.repository.AccessLogManager;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.EkycConstants;
//import in.codifi.ambalal.utilities.MessageConstants;
import in.codifi.ambalal.error.utility.ErrorMessageConstants;
import in.codifi.api.utilities.StoreErrorLogs;
import io.quarkus.logging.Log;

@ApplicationScoped
public class ErrorHandling {
	@Inject
	CommonMethods commonMethods;
	@Inject
	StoreErrorLogs storeErrorLogs;
	//@Inject
	//AccessLogManager accessLogManager;

	/**
	 * Method to handle errors
	 * 
	 * @author vinisha
	 * @param applicationId
	 * @param endPoint
	 * @param module
	 * @param reqOriginatigionIp
	 * @param deviceType
	 * @param deviceId
	 * @param errorCode
	 * @param apiType
	 * @param method
	 * @param className
	 * @param errorMessage
	 */
	public ResponseModel handleErrors(String clientId, String endPoint, String module, String errorCode,
			String apiType, String method, String className, String errorMessage, String response) {
		ResponseModel responseModel = new ResponseModel();
		try {
			Log.error(errorMessage);
			responseModel.setStat(EkycConstants.FAILED_STATUS);
			responseModel.setMessage(EkycConstants.FAILED_MSG);
			responseModel.setErrorCode(errorCode);
			responseModel.setReason(response);
			commonMethods.saveLog(clientId, endPoint, module, errorCode, apiType, method, className, errorMessage);

			commonMethods.sendErrorMail(errorMessage, errorCode, className, method, endPoint, clientId); // mail to
																												// user
		} catch (Exception e) {
			e.printStackTrace();
			commonMethods.sendErrorMail(e.getMessage(), ErrorCodeConstants.EC009, ErrorMessageConstants.ERROR_HANDLING, "handleErrors",EkycConstants.ERROR_HANDLING, clientId); // mail to
			errorHandles("", "", MessageConstants.MODULE, ErrorCodeConstants.EC009, EkycConstants.INTERNAL_ERR,
					"handleErrors", EkycConstants.ERROR_HANDLING, e.getMessage(), ErrorMessageConstants.ERROR_HANDLING);
		}
		return responseModel;
	}

	private void errorHandles(String clientId, String endPoint, String module, String errorCode, String apiType,
			String method, String className, String errorMessage, String response) {
		handleErrors("", "", MessageConstants.MODULE, ErrorCodeConstants.EC009, EkycConstants.INTERNAL_ERR,
				"handleErrors", EkycConstants.ERROR_HANDLING, errorMessage, ErrorMessageConstants.ERROR_HANDLING);
	}

}

