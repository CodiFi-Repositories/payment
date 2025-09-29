package in.codifi.ambalal.service;

import in.codifi.ambalal.entity.TblAdminControlEntity;
import in.codifi.ambalal.model.TblAdminControlRequest;
import in.codifi.ambalal.model.AdminControlRequest;
import in.codifi.ambalal.model.ResponseModel;
import in.codifi.ambalal.repository.AdminControlRepository;
import in.codifi.ambalal.repository.TblAdminControlRepository;
import in.codifi.ambalal.service.spec.BaseTblAdminControlService;
import in.codifi.ambalal.error.utility.ErrorCodeConstants;
import in.codifi.ambalal.error.utility.ErrorMessageConstants;
import in.codifi.ambalal.error.utility.MessageConstants;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.api.utilities.EkycEndpointConstants;
import in.codifi.ambalal.error.utility.ErrorHandling;
import in.codifi.api.utilities.CommonMethods;
//import in.codifi.ambalal.model.AdminControlRequest;KraKeyValueEntity

import in.codifi.ambalal.entity.KraKeyValueEntity;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CustomTblAdminControlService implements BaseTblAdminControlService {

    @Inject
    TblAdminControlRepository repository;

    @Inject
    ErrorHandling errorHandling;
    
    @Inject
    CommonMethods commonMethods;
    
    @Inject
    AdminControlRepository adminRepository;

    
//    @Override
//    public ResponseModel saveTblAdminControls(TblAdminControlRequest request) {
//        ResponseModel response = new ResponseModel();
//
//        try {
//            TblAdminControlEntity entity = new TblAdminControlEntity();
//            entity.setUserId(request.getUserId());
//            entity.setBackoffice(request.isBackoffice());
//            entity.setRms(request.isRms());
//            entity.setGlobe(request.isGlobe());
//            entity.setAutomatic(request.isAutomatic());
//            entity.setManual(request.isManual());
//
//            repository.save(entity);
//
//            response.setMessage(EkycConstants.DATA_SAVED_SUCCESSFULLY);
//            response.setStat(EkycConstants.SUCCESS_STATUS);
//           // response.setErrorCode(null);
//            //response.setReason(null);
//        } catch (Exception e) {
//            response = commonMethods.constructFailedMsg(EkycConstants.DB_SAVE_EXCEPTION, ErrorCodeConstants.EC019);
//        
//
//	        errorHandling.handleErrors("", EkycEndpointConstants.SAVE_ADMIN_CONTROLS, MessageConstants.MODULE,
//	        		ErrorCodeConstants.EC019, EkycConstants.INTERNAL_ERR, EkycConstants.DATA_SAVE_FAILED,
//	                EkycConstants.ADMIN_CLASS, e.getMessage(), ErrorMessageConstants.SAVE_ADMIN_CONTROLS_FAILED_MESSAGE);
//        }
//
//        return response;
//    }
//
//    @Override
//    public ResponseModel getAdminControlByUserId(String userId) {
//        ResponseModel response = new ResponseModel();
//
//        try {
//            TblAdminControlEntity entity = repository.findByUserId(userId);
//
//            if (entity != null) {
//                TblAdminControlRequest data = new TblAdminControlRequest();
//                data.setUserId(entity.getUserId());
//                data.setBackoffice(entity.getBackoffice());
//                data.setRms(entity.getRms());
//                data.setGlobe(entity.getGlobe());
//                data.setAutomatic(entity.getAutomatic());
//                data.setManual(entity.getManual());
//
//                response.setMessage(EkycConstants.DATA_FOUND);
//                response.setStat(EkycConstants.SUCCESS_STATUS);
//                response.setErrorCode(null);
//                response.setReason(null);
//                response.setResult(data);
//            } else {
//                response = commonMethods.constructFailedMsg(EkycConstants.NO_MATCHED_USER, ErrorCodeConstants.EC020);
//                response.setMessage(EkycConstants.NO_DATA_FOUND); // Optional display message
//            }
//        } catch (Exception e) {
//            response = commonMethods.constructFailedMsg(e.getMessage(), ErrorCodeConstants.EC020);
//        
//            
//            errorHandling.handleErrors("", EkycEndpointConstants.FETCH_ADMIN_CONTROLS, MessageConstants.MODULE,
//	        		ErrorCodeConstants.EC020, EkycConstants.INTERNAL_ERR, EkycConstants.EXCEPTION_OCCURRED,
//	                EkycConstants.ADMIN_CLASS, e.getMessage(), ErrorMessageConstants.FETCH_ADMIN_CONTROLS_FAILED_MESSAGE);
//        }
//
//        return response;
//    }

	
    @Override
    public ResponseModel updateControl(List<AdminControlRequest> requestList) {
        ResponseModel response = new ResponseModel();

        try {
            for (AdminControlRequest request : requestList) {
                Optional<KraKeyValueEntity> entityOpt = adminRepository.findByKraKey(request.getDataKey());

                if (entityOpt.isPresent()) {
                    KraKeyValueEntity entity = entityOpt.get();
                    entity.setKraValue(request.isDataValue());  // true → 1, false → 0 in DB
                    adminRepository.save(entity);
                } else  {
	                response = commonMethods.constructFailedMsg(MessageConstants.NOT_MATCHED_KRAKEY, ErrorCodeConstants.EC023);
	                response.setMessage(EkycConstants.FAILED_MSG);
	            }
            }

            response.setStat(EkycConstants.SUCCESS_STATUS);
            response.setMessage(EkycConstants.UPDATE_SUCCESSFULLY);
        
        } catch (Exception e) {
            e.printStackTrace();
           
          
            ResponseModel responseModel = commonMethods.constructFailedMsg(EkycConstants.FAILED_UPDATE_CONTROL,ErrorCodeConstants.EC023);
		    responseModel.setMessage(EkycConstants.INTERNAL_ERR);
		    
		     errorHandling.handleErrors("", EkycEndpointConstants.UPDATE_ADMIN_CONTROLS, MessageConstants.MODULE,
		        		ErrorCodeConstants.EC023, EkycConstants.INTERNAL_ERR, EkycConstants.EXCEPTION_OCCURRED,
		                EkycConstants.ADMIN_CLASS, e.getMessage(), ErrorMessageConstants.UPDATE_ADMIN_CONTROLS_FAILED_MESSAGE);
        }

        return response;
    }

}
