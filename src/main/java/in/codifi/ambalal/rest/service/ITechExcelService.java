package in.codifi.ambalal.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import in.codifi.ambalal.model.LoginRequest;
import in.codifi.ambalal.model.TechLoginResponse;
import in.codifi.ambalal.model.TechReceiptRequestModel;

@RegisterRestClient(configKey = "config-techexcel")
@RegisterClientHeaders
public interface ITechExcelService {

	
	/**
	 * Method to getAccess token
	 * 
	 * @param name and password
	 * @return
	 * @author Vennila
	 */  
	
	
	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@APIResponse(description = "")	
	String getAccessToken(@RequestBody LoginRequest req);

	
	
	/**
	 * Method to update tech excel data 
	 * 
	 * @param TechReceiptRequestModel 
	 * @return TechLoginResponse
	 * @author Vennila
	 */  
	
	@Path("/entry/receipt_normal")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	TechLoginResponse updateTechExcel(@HeaderParam("Authorization") String authToken, TechReceiptRequestModel req);


}
