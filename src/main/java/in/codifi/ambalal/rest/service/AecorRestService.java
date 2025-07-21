package in.codifi.ambalal.rest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.ambalal.entity.CredentialKey;
import in.codifi.ambalal.entity.KraKeyValueEntity;
import in.codifi.ambalal.entity.RmsUpdateResponseEntity;
import in.codifi.ambalal.error.utility.ErrorCodeConstants;
import in.codifi.ambalal.error.utility.ErrorMessageConstants;
import in.codifi.ambalal.error.utility.MessageConstants;
import in.codifi.ambalal.repository.CredentialKeyRepositiory;
import in.codifi.ambalal.repository.KraKeyValueRepository;
import in.codifi.ambalal.repository.RmsUpdateResponseRepository;
import in.codifi.ambalal.rms.model.AccessTokenRequest;
import in.codifi.ambalal.rms.model.AccessTokenResponse;
import in.codifi.ambalal.rms.model.RmsUpdateRequest;
import in.codifi.ambalal.rms.model.RmsUpdateResponse;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.api.utilities.EkycEndpointConstants;
import in.codifi.ambalal.error.utility.ErrorHandling;
import in.codifi.ambalal.model.ResponseModel;

@ApplicationScoped
public class AecorRestService {

	@Inject
	@RestClient
	IAecorRestService aecorRestService;
	@Inject
	CredentialKeyRepositiory credentialKeyRepositiory;
	@Inject
	KraKeyValueRepository kraKeyValueRepository;

	@Inject
	RmsUpdateResponseRepository rmsUpdateResponseRepository;
	@Inject
	ErrorHandling errorHandling;
	
	public String getJwtToken() {
	    ResponseModel responseModel = null;
	    AccessTokenRequest tokenRequest = new AccessTokenRequest();

	    try {
	        // Step 1: Check if RMS is allowed via admin config
	        List<KraKeyValueEntity> kraKeyValues = kraKeyValueRepository.findByMasterIdAndMasterName("01", "Payments");

	        boolean isRmsAllowed = kraKeyValues.stream()
	            .anyMatch(k -> "RMS".equalsIgnoreCase(k.getKraKey()) && Boolean.TRUE.equals(k.getKraValue()));

	        if (!isRmsAllowed) {
	            responseModel = new ResponseModel();
	            responseModel.setStat(EkycConstants.FAILED_STATUS);
	            responseModel.setMessage(EkycConstants.RMS_FALSE);
	            responseModel.setErrorCode(ErrorCodeConstants.EC025); // Specific to RMS config
	            responseModel.setReason(EkycConstants.RMS_NOT_USED);
	            return null; // or return ""; or throw a custom exception if needed
	        }

	        // Step 2: Fetch credentials for RMS login
	        List<CredentialKey> credentialsList = credentialKeyRepositiory.findByType("rms");

	        Map<String, String> credentialsMap = new HashMap<>();
	        for (CredentialKey credential : credentialsList) {
	            credentialsMap.put(credential.getKey(), credential.getValue());
	        }

	        String loginId = credentialsMap.get("LoginId");
	        String password = credentialsMap.get("Password");

	        tokenRequest.setUserID(loginId);
	        tokenRequest.setPassword(password);

	        // Step 3: Perform login via REST service
	        AccessTokenResponse response = aecorRestService.login(tokenRequest);

	        // Step 4: Log the response
	        ObjectMapper obj = new ObjectMapper();
	        try {
	            System.out.println("The response: " + obj.writeValueAsString(response));
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	            errorHandling.handleErrors("",
	                EkycEndpointConstants.RMS_UPDATE, MessageConstants.MODULE, ErrorCodeConstants.EC001,
	                EkycConstants.INTERNAL_ERR, EkycConstants.RMS_UPDATION, EkycConstants.RMS_CLASS, e.getMessage(),
	                ErrorMessageConstants.RMS_UPDATE);
	        }

	        return response.getResult().getToken(); // assuming getResult() is not null and contains token
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        errorHandling.handleErrors("",
	            EkycEndpointConstants.RMS_UPDATE, MessageConstants.MODULE, ErrorCodeConstants.EC001,
	            EkycConstants.INTERNAL_ERR, EkycConstants.RMS_UPDATION, EkycConstants.RMS_CLASS, ex.getMessage(),
	            ErrorMessageConstants.RMS_UPDATE);
	        return null;
	    }
	}

	public RmsUpdateResponse updateRmsLimitFields(String clientId, Double amount) {
		RmsUpdateResponse response = null;
		String token = getJwtToken();
		System.out.println("the token" + token);
		RmsUpdateRequest request = new RmsUpdateRequest();
		List<CredentialKey> credentialsList = credentialKeyRepositiory.findByType("rms");

		Map<String, String> credentialsMap = new HashMap<>();
		for (CredentialKey credential : credentialsList) {
			credentialsMap.put(credential.getKey(), credential.getValue());
		}

		// Example: Get specific keys
		String loginId = credentialsMap.get("LoginId"); // or use "userID"
		request.setLoginId(loginId);
		request.setClientId(clientId);
		request.setPayInAmout(amount);
		request.setAdhocAll(amount);

		response = aecorRestService.updateRmsLimitFields(token, request);
		if (response.getType().equalsIgnoreCase("success")) {
			storeRmsUpdateResponse(response.getType(), response.getCode(), response.getDescription());
		}
		return response;
	}

	public void storeRmsUpdateResponse(String type, String code, String description) {
		RmsUpdateResponseEntity entity = new RmsUpdateResponseEntity();
		entity.setType(type);
		entity.setCode(code);
		entity.setDescription(description);
		rmsUpdateResponseRepository.save(entity);
	}

}