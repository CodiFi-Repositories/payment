package in.codifi.ambalal.rest.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.ambalal.entity.CredentialKey;
import in.codifi.ambalal.entity.KraKeyValueEntity;
import in.codifi.ambalal.entity.PaymentTransactionEntity;
import in.codifi.ambalal.error.utility.ErrorCodeConstants;
import in.codifi.ambalal.error.utility.ErrorHandling;
import in.codifi.ambalal.error.utility.ErrorMessageConstants;
import in.codifi.ambalal.error.utility.MessageConstants;
import in.codifi.ambalal.model.LoginRequest;
import in.codifi.ambalal.model.TechLoginResponse;
import in.codifi.ambalal.model.TechReceiptRequestModel;
import in.codifi.ambalal.repository.AccessLogManager;
import in.codifi.ambalal.repository.CredentialKeyRepositiory;
import in.codifi.ambalal.repository.KraKeyValueRepository;
import in.codifi.ambalal.repository.PaymentTransactionRepository;
import in.codifi.ambalal.model.ResponseModel;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.api.utilities.EkycEndpointConstants;

@ApplicationScoped
public class TechExcelService {

	@Inject
	@RestClient
	ITechExcelService ItechExcelService;
	@Inject
	CredentialKeyRepositiory credentialKeyRepositiory;
	@Inject
	PaymentTransactionRepository paymentRepository;
	@Inject
	AccessLogManager accessLogManager;
	@Inject
	ErrorHandling errorHandling;
	@Inject
	KraKeyValueRepository kraKeyValueRepository;


	public String login() {
		String response = null;
		ResponseModel responseModel = null;
		try {
			
			List<KraKeyValueEntity> kraKeyValues = kraKeyValueRepository.findByMasterIdAndMasterName("01", "Payments");

	        boolean isTechExcelAllowed = kraKeyValues.stream()
	            .anyMatch(k -> "TechExcel".equalsIgnoreCase(k.getKraKey()) && k.getKraValue());

	        if (!isTechExcelAllowed) {
	        	//ResponseModel responseModel = new responseModel();
	            responseModel.setStat(EkycConstants.FAILED_STATUS);
	            responseModel.setMessage(EkycConstants.TECHEXCEL_FALSE);
	            responseModel.setErrorCode(ErrorCodeConstants.EC026); // Specific to TechExcel config
	            responseModel.setReason(EkycConstants.TECHEXCEL_NOT_USED);
	            
	            System.out.println("THE TECH EXCEL VALUE IS 0");
	            return null; // or return ""; based on your flow
	        }
			
			List<CredentialKey> credentialsList = credentialKeyRepositiory.findByType("techExcel");
			Map<String, String> credentialsMap = new HashMap<>();
			for (CredentialKey credential : credentialsList) {
				credentialsMap.put(credential.getKey(), credential.getValue());
			}
			if (credentialsList != null && !credentialsList.isEmpty()) {
				LoginRequest req = new LoginRequest();
				req.setName(credentialsMap.getOrDefault("name", ""));
				req.setPassword(credentialsMap.getOrDefault("password", ""));
				response = ItechExcelService.getAccessToken(req);
				ObjectMapper obj = new ObjectMapper();
				accessLogManager.insertRestAccessLogsIntoDB(null,obj.writeValueAsString(req), obj.writeValueAsString(response), "techExcel-Login",
						"/techExcel/backofficeLogin");
				System.out.println("the response" + response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorHandling.handleErrors("",
					EkycEndpointConstants.TECHEXCEL_TOKEN, MessageConstants.MODULE, ErrorCodeConstants.EC005,
					EkycConstants.INTERNAL_ERR, EkycConstants.TECHEXCEL_TOKEN, EkycConstants.TECHEXCEL_CLASS, e.getMessage(),
					ErrorMessageConstants.TECHEXCEL_TOKEN);
		}
		return response;
	}

	public TechLoginResponse updateTechExcel(String userId, String refNo, Double amt, String bankAccouNo, Long id) {
		TechLoginResponse response = null;
		try {
			String token = login();
			/**
			 * set the defalut date
			 */

			// Format 1: "dd/MM/yyyy"
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String formattedDate1 = LocalDate.now().format(formatter1);

			// Format 2: "yyyy-MM-dd HH:mm:ss"
			DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formattedDate2 = LocalDateTime.now().format(formatter2);

			List<CredentialKey> credentialsList = credentialKeyRepositiory.findByType("techExcel");
			Map<String, String> credentialsMap = new HashMap<>();
			for (CredentialKey credential : credentialsList) {
				credentialsMap.put(credential.getKey(), credential.getValue());
			}
			TechReceiptRequestModel req = new TechReceiptRequestModel();
			req.setVoucherDate(formattedDate1);
			req.setAccountCode(userId);
			req.setCompanyCode(credentialsMap.getOrDefault("companycode", ""));
			req.setChequeNo(refNo);
			req.setAmount(amt);
			req.setPostingBankAccount(credentialsMap.getOrDefault("postingbankaccount", ""));
			req.setBankAccountNumber(bankAccouNo);
			req.setNarration(refNo);
			req.setEntryType(credentialsMap.getOrDefault("entrytype", ""));
			req.setMode(credentialsMap.getOrDefault("mode", ""));
			req.setActualTime(formattedDate2);
			ObjectMapper obj = new ObjectMapper();
			obj.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			String jsonPayload = obj.writeValueAsString(req);
			System.out.println("Sending JSON: " + jsonPayload);
			String rawToken = token.replace("\"", ""); // remove any quotes
			String authToken = "Bearer " + rawToken;
			System.out.println("Sending authToken: " + authToken);

			response = ItechExcelService.updateTechExcel(authToken, req);
			accessLogManager.insertRestAccessLogsIntoDB(userId, obj.writeValueAsString(req), obj.writeValueAsString(response),
					"updateTechExcel", "/techExcel/updatestatus");
			if (response.getSuccess().equalsIgnoreCase("True")) {
				Optional<PaymentTransactionEntity> paymentDT = paymentRepository.findById(id);
				if (!paymentDT.isEmpty()) {
					PaymentTransactionEntity res = paymentDT.get();
					res.setIsUpdateTechexcel(true);
					paymentRepository.save(res);
				}
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			errorHandling.handleErrors(userId,
					EkycEndpointConstants.TECHEXCEL_UPDATE, MessageConstants.MODULE, ErrorCodeConstants.EC006,
					EkycConstants.INTERNAL_ERR, EkycConstants.TECHEXCEL_UPDATION, EkycConstants.TECHEXCEL_CLASS, e.getMessage(),
					ErrorMessageConstants.TECHEXCEL_UPDATE);
		}
		return response;
	}

}