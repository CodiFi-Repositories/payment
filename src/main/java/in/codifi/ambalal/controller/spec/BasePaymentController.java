package in.codifi.ambalal.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public interface BasePaymentController {

	@Path("/getWebHookStatus")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	String getWebHookStatus(String webResponse);

	@Path("/test")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	String testMethod();

	@Path("/getWebHookStatus")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWebHookStatus(MultivaluedMap<String, String> formParams);

	/**
	 * 
	 * capture the webhook response for webhook
	 *
	 * 06-May-2025
	 * 
	 * @author Vennila
	 */

	@Path("/getRazorpayWebHookStatus")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	String getRazorpayWebHookStatus(String webResponse);

}
