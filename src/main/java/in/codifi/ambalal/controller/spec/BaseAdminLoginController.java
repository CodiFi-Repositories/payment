package in.codifi.ambalal.controller.spec;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import in.codifi.ambalal.model.ResponseModel;
import in.codifi.ambalal.model.AdminUserCreateRequest;


 
public interface BaseAdminLoginController {

	/**
	 * Method to loginUser
	 *
	 * @author vennila
	 * @date 13-Aug-2024
	 * @param applicationId
	 * @param type
	 * @return
	 */
	@Path("/userLogIn")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Method to verify login")
	public ResponseModel loginUser(@NotNull @QueryParam("emailID") String emailID,
			@NotNull @QueryParam("password") String password);
	
	    @POST
	    @Path("/create")
	    @Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
	    ResponseModel createAdminUser(AdminUserCreateRequest request);

}
