package in.codifi.ambalal.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import in.codifi.ambalal.controller.spec.BaseGlobeApiController;
import in.codifi.ambalal.entity.PaymentTransactionEntity;
import in.codifi.ambalal.model.AccessTokenResponse;
import in.codifi.ambalal.model.AllocationResponse;
import in.codifi.ambalal.model.StatusInquiryResponse;
import in.codifi.ambalal.rest.service.GlobeRestService;

@Path("/globe") // The path here is the base for the whole controller
public class CustomGlobeApiController implements BaseGlobeApiController {

	@Inject
	GlobeRestService globeRestService;

	@Override
	public AccessTokenResponse testMethod() {
		return globeRestService.getaccessToken();
	}

	@Override
	public AllocationResponse updateCallAllocationApi(PaymentTransactionEntity paymentTransactionEntity) {
		return globeRestService.callAllocationApi(paymentTransactionEntity.getClientCode(), paymentTransactionEntity.getAmount());
	}

	@Override
	public StatusInquiryResponse CallStatusInquiry(String msgId) {
		return globeRestService.callStatusInquiry(msgId);
	}

}
