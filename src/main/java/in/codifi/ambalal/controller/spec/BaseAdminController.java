package in.codifi.ambalal.controller.spec;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import in.codifi.ambalal.model.PaymentStatusRequest;
import in.codifi.ambalal.model.StatusRequest;


public interface BaseAdminController {

    @POST
    @Path("/fetchfailedTransaction")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response getStatusByDate(StatusRequest request);
    
    
    @POST
    @Path("/paymentStatus")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response getPaymentStatus(PaymentStatusRequest request);
    
    @POST
    @Path("/paymentStatus/download")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    Response downloadPaymentStatusExcel(PaymentStatusRequest request);
}
