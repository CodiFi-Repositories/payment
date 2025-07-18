package in.codifi.ambalal.controller.spec;

import in.codifi.ambalal.model.TblAdminControlRequest;
import in.codifi.ambalal.model.AdminControlRequest;
import in.codifi.ambalal.model.ResponseModel;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public interface BaseTblAdminControlController {

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response saveTblAdminControls(TblAdminControlRequest request);
    
    @GET
    @Path("/fetch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response fetchTblAdminControls(TblAdminControlRequest request);


  
//    ResponseModel updateControl(AdminControlRequest request);

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateControl(List<AdminControlRequest> requestList);
}


