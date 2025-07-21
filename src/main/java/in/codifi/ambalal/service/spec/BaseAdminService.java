package in.codifi.ambalal.service.spec;

import java.util.List;

import javax.ws.rs.core.Response;
import in.codifi.ambalal.model.*;;

public interface BaseAdminService {

    /**
     * Fetches payment transaction status records by date.
     * This includes both Atom and Razorpay transactions, based on txnDate.
     *
     * @param date The transaction date in partial format (e.g. "2025-06-10")
     * @return Response containing list of StatusResponse objects
     */
    Response getStatusByDate(String date);
    
    ResponseModel getPaymentStatus(PaymentStatusRequest request);

}