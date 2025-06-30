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
import in.codifi.ambalal.entity.RmsUpdateResponseEntity;
import in.codifi.ambalal.error.utility.ErrorCodeConstants;
import in.codifi.ambalal.error.utility.ErrorMessageConstants;
import in.codifi.ambalal.error.utility.MessageConstants;
import in.codifi.ambalal.repository.CredentialKeyRepositiory;
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
	RmsUpdateResponseRepository rmsUpdateResponseRepository;
	@Inject
	ErrorHandling errorHandling;
	
	public String getJwtToken() {
		ResponseModel responseModel = new ResponseModel();
		AccessTokenRequest tokenRequest = new AccessTokenRequest();
		List<CredentialKey> credentialsList = credentialKeyRepositiory.findByType("rms");

		Map<String, String> credentialsMap = new HashMap<>();
		for (CredentialKey credential : credentialsList) {
			credentialsMap.put(credential.getKey(), credential.getValue());
		}

		// Example: Get specific keys
		String loginId = credentialsMap.get("LoginId"); // or use "userID"
		String password = credentialsMap.get("Password");

		tokenRequest.setUserID(loginId);
		tokenRequest.setPassword(password);

		AccessTokenResponse response = aecorRestService.login(tokenRequest);
		ObjectMapper obj=new ObjectMapper();
		
		try {
			System.out.println("the response"+obj.writeValueAsString(response));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		errorHandling.handleErrors("",
					EkycEndpointConstants.RMS_UPDATE, MessageConstants.MODULE, ErrorCodeConstants.EC001,
					EkycConstants.INTERNAL_ERR, EkycConstants.RMS_UPDATION, EkycConstants.RMS_CLASS, e.getMessage(),
					ErrorMessageConstants.RMS_UPDATE);
			
		}
		return response.getResult().getToken(); // assuming token is inside result
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