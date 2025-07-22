package in.codifi.ambalal.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.Response;

import in.codifi.ambalal.entity.PaymentTransactionEntity;
import in.codifi.ambalal.error.utility.ErrorCodeConstants;
import in.codifi.ambalal.error.utility.ErrorHandling;
import in.codifi.ambalal.error.utility.ErrorMessageConstants;
import in.codifi.ambalal.error.utility.MessageConstants;
import in.codifi.ambalal.model.PaymentStatusRequest;
import in.codifi.ambalal.model.PaymentStatusResponse;
import in.codifi.ambalal.model.ResponseModel;
import in.codifi.ambalal.model.StatusResponse;
import in.codifi.ambalal.repository.PaymentTransactionRepository;
import in.codifi.ambalal.service.spec.BaseAdminService;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.api.utilities.EkycEndpointConstants;

@ApplicationScoped
public class CustomAdminService implements BaseAdminService {

	@Inject
	PaymentTransactionRepository paymentTransactionRepository;

	@Inject
	ErrorHandling errorHandling;

	@Inject
	EntityManager entityManager;
	
	@Inject 
	CommonMethods commonMethods;

	@Override
	public Response getStatusByDate(String date) {
		// ResponseModel responseModel = new ResponseModel();

		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setLenient(false);
			try {
				sdf.parse(date);

			} catch (ParseException e) {
				e.printStackTrace();

				ResponseModel responseModel = commonMethods.constructFailedMsg(EkycConstants.INVALID_DATE, ErrorCodeConstants.EC016);
				responseModel.setMessage(EkycConstants.FETCH_FAILED_TRANSACTIONS); // Optional: override display message


				errorHandling.handleErrors("", EkycEndpointConstants.FETCH_FAILED_TRANSACTIONS, MessageConstants.MODULE,
						ErrorCodeConstants.EC016, EkycConstants.INVALID_DATE, EkycConstants.FETCH_FAILED_TRANSACTIONS,
						EkycConstants.ADMIN_CLASS, e.getMessage(), ErrorMessageConstants.FETCH_FAILED_TRANSACTIONS);

				return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
			}

			List<PaymentTransactionEntity> atomList = paymentTransactionRepository.findByTxnDateContaining(date);
			List<PaymentTransactionEntity> razorpayList = paymentTransactionRepository
					.findByRazorpayCreatedAtEpochContaining(date);

			List<PaymentTransactionEntity> entityList = new ArrayList<>();
			entityList.addAll(atomList);
			entityList.addAll(razorpayList);

			List<PaymentTransactionEntity> failedList = entityList.stream()
					.filter(entity -> entity.getStatus() != null && entity.getStatus().equalsIgnoreCase("FAILED"))
					.collect(Collectors.toList());

			List<StatusResponse> responseList = failedList.stream().map(entity -> {
				StatusResponse response = new StatusResponse();

				String dateValue = entity.getTxnDate() != null ? entity.getTxnDate()
						: (entity.getRazorpayCreatedAtEpoch() != null
								? String.valueOf(entity.getRazorpayCreatedAtEpoch())
								: "N/A");

				response.setDate(dateValue);

				if (Boolean.TRUE.equals(entity.getIsAtom())) {
					response.setAccountNo(entity.getCustomerAccNo());
					response.setBankName(entity.getBankName());
					response.setAmount(entity.getAmount());
					response.setStatus(entity.getStatus());
				} else if (Boolean.TRUE.equals(entity.getIsRazorpay())) {
					response.setAccountNo(entity.getRazorpayAcountNumber());
					response.setBankName(entity.getRazorpayMethod());
					response.setAmount(entity.getAmountPaid() != null ? entity.getAmountPaid().doubleValue() : 0.0);
					response.setStatus(entity.getStatus());
				} else {
					response.setAccountNo("-");
					response.setBankName("-");
					response.setAmount(0.0);
					response.setStatus("UNKNOWN");
				}

				return response;
			}).collect(Collectors.toList());

			return Response.ok(responseList).build();

		} catch (Exception e) {
			e.printStackTrace();

		    ResponseModel responseModel = commonMethods.constructFailedMsg(EkycConstants.FETCH_FAILED_TRANSACTIONS,ErrorCodeConstants.EC015);
		    responseModel.setMessage(EkycConstants.INTERNAL_ERR);
			
			errorHandling.handleErrors("", EkycEndpointConstants.FETCH_FAILED_TRANSACTIONS, MessageConstants.MODULE,
					ErrorCodeConstants.EC015, EkycConstants.INTERNAL_ERR, EkycConstants.FETCH_FAILED_TRANSACTIONS,
					EkycConstants.ADMIN_CLASS, e.getMessage(), ErrorMessageConstants.FETCH_FAILED_TRANSACTIONS);

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
		}
	}

	@Override
	public ResponseModel getPaymentStatus(PaymentStatusRequest request) {
	    ResponseModel responseModel = new ResponseModel();
	    try {
	        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	        CriteriaQuery<PaymentTransactionEntity> cq = cb.createQuery(PaymentTransactionEntity.class);
	        Root<PaymentTransactionEntity> root = cq.from(PaymentTransactionEntity.class);

	        List<Predicate> predicates = new ArrayList<>();

	        // Filters
	        if (request.getUserId() != null && !request.getUserId().isBlank()) {
	            predicates.add(cb.equal(root.get("clientCode"), request.getUserId()));
	        }

	        if (request.getPaymentMethod() != null && !request.getPaymentMethod().isBlank()) {
	            if (request.getPaymentMethod().equalsIgnoreCase("Razorpay")) {
	                predicates.add(cb.isTrue(root.get("isRazorpay")));
	            } else if (request.getPaymentMethod().equalsIgnoreCase("Atompay")) {
	                predicates.add(cb.isTrue(root.get("isAtom")));
	            }
	        }

	        if (request.getStatus() != null && !request.getStatus().isBlank()) {
	            if (request.getStatus().equalsIgnoreCase("Success")) {
	                predicates.add(cb.or(cb.isTrue(root.get("isUpdateTechexcel")), cb.isTrue(root.get("isUpdateGlobe"))));
	            } else if (request.getStatus().equalsIgnoreCase("Failed") || request.getStatus().equalsIgnoreCase("FAILURE")) {
	                predicates.add(cb.and(
	                        cb.or(cb.isFalse(root.get("isUpdateTechexcel")), cb.isNull(root.get("isUpdateTechexcel"))),
	                        cb.or(cb.isFalse(root.get("isUpdateGlobe")), cb.isNull(root.get("isUpdateGlobe")))));
	            }
	        }

	        if (request.getGlbTechexcelRms() != null && !request.getGlbTechexcelRms().isBlank()) {
	            if (request.getGlbTechexcelRms().equalsIgnoreCase("TechExcel")) {
	                predicates.add(cb.isTrue(root.get("isUpdateTechexcel")));
	            } else if (request.getGlbTechexcelRms().equalsIgnoreCase("Globe")) {
	                predicates.add(cb.isTrue(root.get("isUpdateGlobe")));
	            } else if (request.getGlbTechexcelRms().equalsIgnoreCase("RMS")) {
	                predicates.add(cb.isTrue(root.get("isUpdateRms")));
	            }
	        }


	        // Date filtering
	        if (request.getFromDate() != null && request.getFromTime() != null && request.getToDate() != null && request.getToTime() != null) {
	            try {
	                String fromDateTime = request.getFromDate() + " " + request.getFromTime();
	                String toDateTime = request.getToDate() + " " + request.getToTime();

	                List<Predicate> datePredicates = new ArrayList<>();

	                try {
	                    datePredicates.add(cb.between(root.get("txnDate"), fromDateTime, toDateTime));
	                } catch (Exception ignored) {
	                }

	                try {
	                    datePredicates.add(cb.between(root.get("razorpayCreatedAtEpoch"), fromDateTime, toDateTime));
	                } catch (Exception ignored) {
	                }

	                if (!datePredicates.isEmpty()) {
	                    predicates.add(cb.or(datePredicates.toArray(new Predicate[0])));
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }

	        if (!predicates.isEmpty()) {
	            cq.where(cb.and(predicates.toArray(new Predicate[0])));
	        }

	        List<PaymentTransactionEntity> results = entityManager.createQuery(cq).getResultList();

	        if (!results.isEmpty()) {
	            List<PaymentStatusResponse> responseList = results.stream().map(entity -> {
	                PaymentStatusResponse response = new PaymentStatusResponse();

	                response.setUserId(entity.getClientCode());
	                response.setTransactionId(entity.getRazorpayOrderId() != null ? entity.getRazorpayOrderId() : entity.getTxnId());

	                if (entity.getRazorpayCreatedAtEpoch() != null) {
	                    response.setDate(entity.getRazorpayCreatedAtEpoch().toString());
	                } else if (entity.getTxnDate() != null) {
	                    response.setDate(entity.getTxnDate().toString());
	                } else {
	                    response.setDate("");
	                }

	                if (Boolean.TRUE.equals(entity.getIsRazorpay())) {
	                    response.setAmount(entity.getAmountPaid() != null ? entity.getAmountPaid().toString() : "0.0");
	                    response.setPaymentMethod("Razorpay");
	                } else if (Boolean.TRUE.equals(entity.getIsAtom())) {
	                    response.setAmount(entity.getAmount() != null ? entity.getAmount().toString() : "0.0");
	                    response.setPaymentMethod("Atompay");
	                } else {
	                    if (entity.getAmountPaid() != null) {
	                        response.setAmount(entity.getAmountPaid().toString());
	                        response.setPaymentMethod("Razorpay");
	                    } else {
	                        response.setAmount(entity.getAmount() != null ? entity.getAmount().toString() : "0.0");
	                        response.setPaymentMethod("Atompay");
	                    }
	                }

	                response.setSegment("FO");
	                response.setGlobe(isTrue(entity.getIsUpdateGlobe()) ? "Success" : "Failed");
	                response.setTechExcel(isTrue(entity.getIsUpdateTechexcel()) ? "Success" : "Failed");
	                response.setRms(""); // Placeholder for RMS status, to be updated later


	                return response;
	            }).collect(Collectors.toList());

	            responseModel.setStat(EkycConstants.SUCCESS_STATUS);
	            responseModel.setMessage(EkycConstants.FETCHED_SUCCESSFULLY);
	            responseModel.setErrorCode(null);
	            responseModel.setReason(null);
	            responseModel.setResult(responseList);
	        } else {
	            responseModel = commonMethods.constructFailedMsg(EkycConstants.NO_MATCHED_TRANSACTIONS,ErrorCodeConstants.EC017);
	            responseModel.setMessage(EkycConstants.FETCH_FAILED_TRANSACTIONS); 
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        responseModel = commonMethods.constructFailedMsg(EkycConstants.NO_MATCHED_TRANSACTIONS,ErrorCodeConstants.EC017);
	        responseModel.setMessage(EkycConstants.INTERNAL_ERR); // Optional override
	    
	        errorHandling.handleErrors("", EkycEndpointConstants.NO_MATCHED_TRANSACTIONS, MessageConstants.MODULE,
	        		ErrorCodeConstants.EC017, EkycConstants.INTERNAL_ERR, EkycConstants.NO_MATCHED_TRANSACTIONS,
	                EkycConstants.ADMIN_CLASS, e.getMessage(), ErrorMessageConstants.NO_MATCHED_TRANSACTIONS);
	    }

	    return responseModel;
	}
	private boolean isTrue(Boolean value) {
		return value != null && value;
	} 

}
