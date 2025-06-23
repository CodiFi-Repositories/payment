package in.codifi.ambalal.rest.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.ambalal.entity.AccessTokenRequest;
import in.codifi.ambalal.entity.CredentialKey;
import in.codifi.ambalal.entity.GlobeInquiryResponse;
import in.codifi.ambalal.model.AccessTokenResponse;
import in.codifi.ambalal.model.AllocationData;
import in.codifi.ambalal.model.AllocationRequest;
import in.codifi.ambalal.model.AllocationRequestPayload;
import in.codifi.ambalal.model.AllocationResponse;
import in.codifi.ambalal.model.InquiryResponse;
import in.codifi.ambalal.model.StatusInquiryRequest;
import in.codifi.ambalal.model.StatusInquiryResponse;
import in.codifi.ambalal.repository.CredentialKeyRepositiory;
import in.codifi.ambalal.repository.GlobeInquiryResponseRepository;

@ApplicationScoped
public class GlobeRestService {

	@Inject
	@RestClient
	IGlobeRestService globeRestService;

	@Inject
	CredentialKeyRepositiory credentialKeyRepositiory;

	@Inject
	GlobeInquiryResponseRepository responseRepository;

	/**
	 * Method to getAccess token
	 * 
	 * @param model
	 * @return
	 * @author Vennila
	 * @throws Exception
	 */
	public AccessTokenResponse getaccessToken() {
		AccessTokenResponse apiModel = null;
		try {
			List<CredentialKey> credentialsList = credentialKeyRepositiory.findByType("globe");
			if (credentialsList != null && !credentialsList.isEmpty()) {
				for (CredentialKey credential : credentialsList) {
					if ("authToken".equalsIgnoreCase(credential.getKey())) {
						System.out.println("the credential.getValue()" + credential.getValue());
						AccessTokenRequest request = new AccessTokenRequest();
						request.setAuthorization(credential.getValue()); // or from DB
						apiModel = globeRestService.getAccessToken(request);
						ObjectMapper obj = new ObjectMapper();
						System.out.println("the apiModel object is " + obj.writeValueAsString(apiModel));
						System.out.println("the apiModel" + apiModel.getAccessToken());
						break; // stop after finding the matching key
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return apiModel;
	}

	/**
	 * Method to callAllocationApi
	 * 
	 * @param clientId,amount
	 * @return
	 * @author Vennila
	 * @throws Exception
	 */

	public AllocationResponse callAllocationApi(String clientId, Double amount) {
		try {
			AccessTokenResponse tokenResponse = getaccessToken();
			if (tokenResponse != null && "Success".equalsIgnoreCase(tokenResponse.getStatus())) {

				// Fetch required credentials
				List<CredentialKey> credentialsList = credentialKeyRepositiory.findByType("globe");
				Map<String, String> credentialsMap = new HashMap<>();
				for (CredentialKey credential : credentialsList) {
					credentialsMap.put(credential.getKey(), credential.getValue());
				}

				// Prepare allocation request
				AllocationRequest allocation = new AllocationRequest();
				String curDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(new Date());

				allocation.setCurDate(curDate);
				allocation.setSegment("FO");
				allocation.setCmCode(credentialsMap.getOrDefault("cmCode", ""));
				allocation.setTmCode(credentialsMap.getOrDefault("tmCode", ""));
				allocation.setCpCode("");
				allocation.setCliCode(clientId);
				allocation.setAccType("C");
				allocation.setAmt(amount.toString());
				allocation.setFiller1("");
				allocation.setFiller2("");
				allocation.setFiller3("");
				allocation.setFiller4("");
				allocation.setFiller5("");
				allocation.setFiller6("");
				allocation.setAction("U");

				// Wrap allocation in data payload
				AllocationData allocationData = new AllocationData();
				String uniqueNumber = String.format("%07d", System.currentTimeMillis() % 10000000); // 7 chars
				String tmCode = credentialsMap.getOrDefault("tmCode", "00000"); // 5 chars
				String trDate = new SimpleDateFormat("ddMMyyyy").format(new Date()); // 8 chars (e.g., 23062025)
				String msgId = tmCode + trDate + uniqueNumber; // total 20 chars

				allocationData.setMsgId(msgId);

				System.out.println("the tokjrn id " + allocationData.getMsgId());// message
																					// ID
				allocationData.setRequestType("I");
				allocationData.setAllocationRequest(List.of(allocation));

				// Final payload
				AllocationRequestPayload payload = new AllocationRequestPayload();
				payload.setAccessToken(tokenResponse.getAccessToken());
				payload.setCompanytype(credentialsMap.getOrDefault("companytype", "GCML"));
				payload.setClrtype(credentialsMap.getOrDefault("clrtype", "NCL"));
				payload.setDataFormat("JSON");
				payload.setDataTotalCount("1");
				payload.setData(allocationData);

				return globeRestService.sendAllocation("application/json", payload);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Method to callStatusInquiry
	 * 
	 * @param msgId
	 * @return
	 * @author Vennila
	 * @throws Exception
	 */

	public StatusInquiryResponse callStatusInquiry(String msgId) {
		try {
			AccessTokenResponse tokenResponse = getaccessToken();
			if (tokenResponse != null && "Success".equalsIgnoreCase(tokenResponse.getStatus())) {

				StatusInquiryRequest request = new StatusInquiryRequest();
				request.setAccessToken(tokenResponse.getAccessToken());
				request.setMsgId(msgId);
				request.setDataFormat("JSON");

				StatusInquiryResponse resStatus = globeRestService.statusInquiry("application/json", request);
				saveInquiryResponse(resStatus, msgId);
				return resStatus;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void saveInquiryResponse(StatusInquiryResponse response, String msgId) {
	    try {
	        if (response != null && "Success".equalsIgnoreCase(response.getStatus()) && response.getData() != null) {
	            for (InquiryResponse inquiry : response.getData().getInquiryResponse()) {
	                GlobeInquiryResponse entity = responseRepository.findByMsgId(msgId);
	                if (entity == null) {
	                    entity = new GlobeInquiryResponse();
	                    entity.setMsgId(msgId); // Set once if new
	                }

	                entity.setStatus(response.getStatus());
	                entity.setMessagesCode(response.getMessagesCode());
	                entity.setMessages(response.getMessages());

	                // Convert curDate to java.util.Date
	                Date parsedDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).parse(inquiry.getCurDate());
	                entity.setCurDate(parsedDate);

	                entity.setSegment(inquiry.getSegment());
	                entity.setCmCode(inquiry.getCmCode());
	                entity.setTmCode(inquiry.getTmCode());
	                entity.setCpCode(inquiry.getCpCode());
	                entity.setCliCode(inquiry.getCliCode());
	                entity.setAccType(inquiry.getAccType());
	                entity.setAmt(inquiry.getAmt());
	                entity.setFiller1(inquiry.getFiller1());
	                entity.setFiller2(inquiry.getFiller2());
	                entity.setFiller3(inquiry.getFiller3());
	                entity.setFiller4(inquiry.getFiller4());
	                entity.setFiller5(inquiry.getFiller5());
	                entity.setFiller6(inquiry.getFiller6());
	                entity.setAction(inquiry.getAction());
	                entity.setErrCd(inquiry.getErrCd());

	                responseRepository.save(entity);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


}
