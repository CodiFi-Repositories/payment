package in.codifi.ambalal.controller;

import in.codifi.ambalal.controller.spec.BaseTblAdminControlController;
import in.codifi.ambalal.model.TblAdminControlRequest;
import in.codifi.ambalal.model.AdminControlRequest;
import in.codifi.ambalal.model.ResponseModel;
import in.codifi.ambalal.service.spec.BaseTblAdminControlService;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.ambalal.error.utility.ErrorCodeConstants;
import in.codifi.ambalal.error.utility.ErrorHandling;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/admin/control") // Base path for this controller
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomTblAdminControlController implements BaseTblAdminControlController {

    @Inject
    BaseTblAdminControlService AdminControlServicee;

    @Inject
    ErrorHandling errorHandling;

    @Override
    public Response saveTblAdminControls(TblAdminControlRequest request) {
        return Response.ok(AdminControlServicee.saveTblAdminControls(request)).build();
    }

    
    @Override
    public Response fetchTblAdminControls(TblAdminControlRequest request) {
        return Response.ok(AdminControlServicee.getAdminControlByUserId(request.getUserId())).build();
    }
    
    
    @Override
    public Response updateControl(List<AdminControlRequest> requestList) {
        ResponseModel response = AdminControlServicee.updateControl(requestList);
        return Response.ok(response).build();
    }
    

}
