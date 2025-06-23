package in.codifi.ambalal.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import in.codifi.ambalal.entity.AccessTokenRequest;
import in.codifi.ambalal.model.AccessTokenResponse;
import in.codifi.ambalal.model.AllocationRequestPayload;
import in.codifi.ambalal.model.AllocationResponse;
import in.codifi.ambalal.model.StatusInquiryRequest;
import in.codifi.ambalal.model.StatusInquiryResponse;

@RegisterRestClient(configKey = "config-globe")
@RegisterClientHeaders
public interface IGlobeRestService {

	

	/**
	 * Method to getAccess token
	 * 
	 * @param model
	 * @return
	 * @author Vennila
	 * @throws Exception
	 */  
	@Path("/GetToken")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@APIResponse(description = "")
	AccessTokenResponse getAccessToken(AccessTokenRequest  req);
	
	/**
	 * Method to callAllocationApi
	 * 
	 * @param clientId,amount
	 * @return
	 * @author Vennila
	 * @throws Exception
	 */
	
	
	@Path("/Allocation")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	AllocationResponse sendAllocation(@HeaderParam("Content-Type") String contentType,
	                                   AllocationRequestPayload requestPayload);
	/**
	 * Method to callStatusInquiry
	 * 
	 * @param msgId
	 * @return
	 * @author Vennila
	 * @throws Exception
	 */
	
	
    @Path("/StatusInquiry")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    StatusInquiryResponse statusInquiry(@HeaderParam("Content-Type") String contentType,
                                        StatusInquiryRequest request);

}
