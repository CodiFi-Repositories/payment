package in.codifi.ambalal.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import in.codifi.ambalal.entity.PaymentTransactionEntity;
import in.codifi.ambalal.model.AccessTokenResponse;
import in.codifi.ambalal.model.AllocationResponse;

public interface BaseGlobeApiController {

	
	
	@Path("/getToken")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public AccessTokenResponse testMethod();
	
	
	/**
	 * 
	 *update Call AllocationApi
	 *
	 * 23-June-2025
	 * 
	 * @author Vennila
	 */
    
    
    @Path("/updateCallAllocationApi")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    public  AllocationResponse updateCallAllocationApi(@RequestBody PaymentTransactionEntity paymentTransactionEntity);
    
    
   
}
