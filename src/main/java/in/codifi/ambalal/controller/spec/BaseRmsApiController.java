package in.codifi.ambalal.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import in.codifi.ambalal.rms.model.RmsUpdateResponse;

public interface BaseRmsApiController {

	@Path("/updateRmsLimits")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RmsUpdateResponse CallStatusInquiry(@QueryParam("clientId") String clientId,
			@QueryParam("amount") Double amount);

}
