package in.codifi.ambalal.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import in.codifi.ambalal.model.TechLoginResponse;

public interface BaseTechExcelController {

	/**
	 * 
	 * To get the Token for tech excel
	 *
	 * 24-June-2025
	 * 
	 * @author Vennila
	 */
	@GET
	@Path("/backofficeLogin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	String login();

	/**
	 * 
	 * To get the Token for tech excel to update the status
	 *
	 * 24-June-2025
	 * 
	 * @author Vennila
	 */
	@GET
	@Path("/updatestatus")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public TechLoginResponse  processReceipt(@QueryParam("userId")  String userId,@QueryParam("chekNo")  String chekNo,@QueryParam("amt")  Double amt,@QueryParam("bankAccouNo")  String bankAccouNo);
}
