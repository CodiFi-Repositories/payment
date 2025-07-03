package in.codifi.ambalal.rest.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.ambalal.entity.AccessTokenRequest;
import in.codifi.ambalal.entity.CredentialKey;
import in.codifi.ambalal.entity.GlobeInquiryResponse;
import in.codifi.ambalal.entity.PaymentTransactionEntity;
import in.codifi.ambalal.error.utility.ErrorCodeConstants;
import in.codifi.ambalal.error.utility.ErrorHandling;
import in.codifi.ambalal.error.utility.ErrorMessageConstants;
import in.codifi.ambalal.error.utility.MessageConstants;
import in.codifi.ambalal.model.AccessTokenResponse;
import in.codifi.ambalal.model.AllocationData;
import in.codifi.ambalal.model.AllocationRequest;
import in.codifi.ambalal.model.AllocationRequestPayload;
import in.codifi.ambalal.model.AllocationResponse;
import in.codifi.ambalal.model.InquiryResponse;
import in.codifi.ambalal.model.StatusInquiryRequest;
import in.codifi.ambalal.model.StatusInquiryResponse;
import in.codifi.ambalal.repository.AccessLogManager;
import in.codifi.ambalal.repository.CredentialKeyRepositiory;
import in.codifi.ambalal.repository.GlobeInquiryResponseRepository;
import in.codifi.ambalal.repository.PaymentTransactionRepository;
import in.codifi.ambalal.model.ResponseModel;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.api.utilities.EkycEndpointConstants;

@ApplicationScoped
public class GlobeRestService {

	@Inject
	@RestClient
	IGlobeRestService globeRestService;

	@Inject
	CredentialKeyRepositiory credentialKeyRepositiory;

	@Inject
	ErrorHandling errorHandling;
	@Inject
	GlobeInquiryResponseRepository responseRepository;
	@Inject
	PaymentTransactionRepository paymentRepository;
	@Inject
	AccessLogManager accessLogManager;

	@Inject
	TechExcelService techExcelService;
	
	/**
	 * Method to getAccess token
	 * 
	 * @param model
	 * @return
	 * @author Vennila
	 * @throws Exception
	 */
	public AccessTokenResponse getaccessToken() {
		ResponseModel responseModel = new ResponseModel();
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
						accessLogManager.insertRestAccessLogsIntoDB(null, "Globe", obj.writeValueAsString(apiModel), "getaccessToken", "/globe/getToken");
						break; // stop after finding the matching key
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorHandling.handleErrors("",
					EkycEndpointConstants.GLOBE_GET_TOKEN, MessageConstants.MODULE, ErrorCodeConstants.EC002,
					EkycConstants.INTERNAL_ERR, EkycConstants.GLB_TOKEN, EkycConstants.GLB_CLASS, e.getMessage(),
					ErrorMessageConstants.GLOBE_TOKEN);
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

	public AllocationResponse callAllocationApi(String clientId, Double amount,String referenceNo, Long id,String accNo) {
		try {
			AllocationResponse returnResponse=null;
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

				returnResponse= globeRestService.sendAllocation("application/json", payload);
				ObjectMapper obj = new ObjectMapper();
				accessLogManager.insertRestAccessLogsIntoDB(null, "Globe", obj.writeValueAsString(returnResponse), "callAllocationApi", "/globe/updateCallAllocationApi");
				if(returnResponse.getMessagesCode().equalsIgnoreCase("100100")) {
					Optional<PaymentTransactionEntity> paymentDT = paymentRepository
							.findById(id);
					if(!paymentDT.isEmpty()) {
						PaymentTransactionEntity res=paymentDT.get();
						res.setIsUpdateGlobe(true);
						paymentRepository.save(res);
						
//						if (!Boolean.TRUE.equals(responseEntity.getIsUpdateTechexcel())) {
							techExcelService.updateTechExcel(clientId,
									referenceNo,
									amount,
									accNo,id);
//						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorHandling.handleErrors(clientId,
					EkycEndpointConstants.GLOBE_UPDATE, MessageConstants.MODULE, ErrorCodeConstants.EC003,
					EkycConstants.INTERNAL_ERR, EkycConstants.GLB_UPDATION, EkycConstants.GLB_CLASS, e.getMessage(),
					ErrorMessageConstants.GLOBE_UPDATE);
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
		ResponseModel responseModel = new ResponseModel();
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
			errorHandling.handleErrors(msgId,
					EkycEndpointConstants.GLOBE_INQUIRY, MessageConstants.MODULE, ErrorCodeConstants.EC004,
					EkycConstants.INTERNAL_ERR, EkycConstants.GLB_INQUIRY, EkycConstants.GLB_CLASS, e.getMessage(),
					ErrorMessageConstants.GLOBE_INQUIRY);
		}
		return null;
	}

	public void saveInquiryResponse(StatusInquiryResponse response, String msgId) {
		ResponseModel responseModel = new ResponseModel();
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
	    	errorHandling.handleErrors(msgId,
					EkycEndpointConstants.GLOBE_INQUIRY, MessageConstants.MODULE, ErrorCodeConstants.EC004,
					EkycConstants.INTERNAL_ERR, EkycConstants.GLB_INQUIRY, EkycConstants.GLB_CLASS, e.getMessage(),
					ErrorMessageConstants.GLOBE_INQUIRY);
	    }
	}


}
