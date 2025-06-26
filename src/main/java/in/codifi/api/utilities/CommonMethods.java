package in.codifi.api.utilities;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import in.codifi.ambalal.entity.EmailTemplateEntity;
import in.codifi.ambalal.rms.model.ErrorLogModel;
import in.codifi.ambalal.model.ResponseModel;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.ambalal.error.utility.ErrorHandling;
import in.codifi.ambalal.repository.EmailLogRepository;
import in.codifi.ambalal.repository.EmailTemplateRepository;
//import in.codifi.ambalal.repository.MessageTemplateRepository;
import in.codifi.api.utilities.CommonMail;
import in.codifi.api.utilities.StoreErrorLogs;
import in.codifi.ambalal.entity.EmailLogEntity;
import in.codifi.ambalal.error.utility.ErrorCodeConstants;
import in.codifi.ambalal.error.utility.ErrorMessageConstants;
import in.codifi.ambalal.error.utility.MessageConstants;

@ApplicationScoped
public class CommonMethods {
	
	@Inject
	StoreErrorLogs storeErrorLogs;
	@Inject
	ErrorHandling errorHandling;
	@Inject
	CommonMail commonMail;
	@Inject
	EmailTemplateRepository emailTemplateRepository;
	@Inject
	EmailLogRepository emailLogRepository;
	
	
	
	
	/**
	 * Method to construct Failed method
	 * 
	 * @author prade
	 * @param failesMessage
	 * @return
	 */
	public ResponseModel constructFailedMsg(String pFailesMessage) {
		ResponseModel model = new ResponseModel();
		model.setStat(EkycConstants.FAILED_STATUS);
		model.setMessage(EkycConstants.FAILED_MSG);
		model.setReason(pFailesMessage);
		return model;
	}

	public ResponseModel constructFailedMsg(String pFailesMessage, String errorCode) {
		ResponseModel model = new ResponseModel();
		model.setStat(EkycConstants.FAILED_STATUS);
		model.setMessage(EkycConstants.FAILED_MSG);
		model.setReason(pFailesMessage);
		model.setErrorCode(errorCode);
		return model;
	}
	
	public void storeEmailLog(String pMessage, String pReqSub, String pEmailResponse, String pLogMethod,
			List<String> pMailIds) {
		if (pMessage == null || pEmailResponse == null || pLogMethod == null) {
			throw new IllegalArgumentException("Request, EmailResponse, or logMethod cannot be null.");
		}

		try {
			for (String mailId : pMailIds) {
				EmailLogEntity emailLogEntity = new EmailLogEntity();
				emailLogEntity.setEmailId(mailId);
				emailLogEntity.setLogMethod(pLogMethod);
				emailLogEntity.setReqLogSub(pReqSub);
				emailLogEntity.setReqLog(pMessage);
				emailLogEntity.setResponseLog(pEmailResponse);
				emailLogRepository.save(emailLogEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorHandling.handleErrors("", "", MessageConstants.MODULE, ErrorCodeConstants.EC011,
					EkycConstants.INTERNAL_ERR, "storeEmailLog", EkycConstants.COMMON_METHOD, e.getMessage(),
					ErrorMessageConstants.SEND_MAIL);
		}
	}
	
	public void sendErrorMail(String pErrorMessage, String pErrorCode, String className, String methodName,
			String endPoint, String applicationId) {
		EmailTemplateEntity emailTemplateEntity = emailTemplateRepository.findByKeyData("error");
		if (emailTemplateEntity != null && emailTemplateEntity.getBody() != null
				&& emailTemplateEntity.getSubject() != null && emailTemplateEntity.getToAddress() != null) {
			List<String> toAdd = new ArrayList<>();
			toAdd.add(emailTemplateEntity.getToAddress());
			String bodyMessage = emailTemplateEntity.getBody();
			String body = bodyMessage.replace("{errorMessage}", pErrorMessage).replace("{errorCode}", pErrorCode)
					.replace("{endPoint}", endPoint).replace("{className}", className)
					.replace("{methodName}", methodName).replace("{applicationId}", applicationId);
			String subject = emailTemplateEntity.getSubject();
			if (emailTemplateEntity.getCc() != null) {
				String[] ccAddresses = emailTemplateEntity.getCc().split(",");
				for (String ccAddress : ccAddresses) {
					toAdd.add(ccAddress.trim());
				}
			}
			commonMail.sendMail(toAdd, subject, body);
		}
	}
	

	public void saveLog(String pApplicationId, String endPoint, String module, String errorCode, String apiType,
			String method, String className, String errorMessage) {
		ErrorLogModel errorLogEntity = new ErrorLogModel();
		errorLogEntity.setClientId(pApplicationId);
		errorLogEntity.setMethodName(method);
		errorLogEntity.setReason(errorMessage);
		errorLogEntity.setApiType(apiType);
		errorLogEntity.setModule(module);
		errorLogEntity.setErrorCode(errorCode);
		errorLogEntity.setEndPoint(endPoint);
		errorLogEntity.setClassName(className);
		storeErrorLogs.insertErrorLogsIntoDB(errorLogEntity);
	}

}
