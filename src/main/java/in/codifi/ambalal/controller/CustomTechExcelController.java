package in.codifi.ambalal.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import in.codifi.ambalal.controller.spec.BaseTechExcelController;
import in.codifi.ambalal.model.TechLoginResponse;
import in.codifi.ambalal.rest.service.TechExcelService;

@Path("/techExcel")
public class CustomTechExcelController implements BaseTechExcelController {

    @Inject
    TechExcelService techExcelService;

   


    
    
    @Override
    public String login() {
        return  techExcelService.login();

    }

    @Override
    public TechLoginResponse processReceipt(String userId, String chekNo, Double amt, String bankAccouNo) {
        try {
        	return  techExcelService.updateTechExcel(userId, chekNo, amt, bankAccouNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }
}
