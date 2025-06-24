package in.codifi.ambalal.rest.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.ambalal.entity.CredentialKey;
import in.codifi.ambalal.model.LoginRequest;
import in.codifi.ambalal.model.TechLoginResponse;
import in.codifi.ambalal.model.TechReceiptRequestModel;
import in.codifi.ambalal.repository.CredentialKeyRepositiory;

@ApplicationScoped
public class TechExcelService {

	@Inject
	@RestClient
	ITechExcelService ItechExcelService;
	@Inject
	CredentialKeyRepositiory credentialKeyRepositiory;

	public String login() {
		String response = null;
		try {
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
				System.out.println("the response" + response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public TechLoginResponse updateTechExcel(String userId, String chekNo, Double amt, String bankAccouNo) {
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
			req.setCompanyCode(credentialsMap.getOrDefault("comapnycode", ""));
			req.setChequeNo(chekNo);
			req.setAmount(amt);
			req.setPostingBankAccount(credentialsMap.getOrDefault("postingbankaccount", ""));
			req.setBankAccountNumber(bankAccouNo);
			req.setNarration(credentialsMap.getOrDefault("narration", ""));
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}