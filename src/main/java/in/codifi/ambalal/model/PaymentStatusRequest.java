package in.codifi.ambalal.model;
import lombok.Data;

@Data
public class PaymentStatusRequest {
	

	
	    private String fromDate;
	    private String fromTime;
	    private String toDate;
	    private String toTime;
	    private String userId;
	    private String paymentMethod;
	    private String glbTechexcelRms;
	    private String status;
	

}
