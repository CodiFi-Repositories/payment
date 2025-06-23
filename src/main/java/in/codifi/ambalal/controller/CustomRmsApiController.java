package in.codifi.ambalal.controller;

import javax.inject.Inject;

import in.codifi.ambalal.controller.spec.BaseRmsApiController;
import in.codifi.ambalal.rest.service.AecorRestService;
import in.codifi.ambalal.rms.model.RmsUpdateResponse;

public class CustomRmsApiController implements BaseRmsApiController {

	@Inject
	AecorRestService aecorRestService;

	@Override
	public RmsUpdateResponse CallStatusInquiry(String clientId, Double amount) {

		return aecorRestService.updateRmsLimitFields(clientId, amount);
	}

}
