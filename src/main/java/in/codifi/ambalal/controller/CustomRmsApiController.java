package in.codifi.ambalal.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import in.codifi.ambalal.controller.spec.BaseRmsApiController;
import in.codifi.ambalal.rest.service.AecorRestService;
import in.codifi.ambalal.rms.model.RmsUpdateResponse;

@Path("/rmsUpdate")
public class CustomRmsApiController implements BaseRmsApiController {

	@Inject
	AecorRestService aecorRestService;

	@Override
	public RmsUpdateResponse CallStatusInquiry(String clientId, Double amount) {

		return aecorRestService.updateRmsLimitFields(clientId, amount);
	}

}
