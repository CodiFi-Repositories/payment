package in.codifi.ambalal.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

import in.codifi.ambalal.entity.PaymentTransactionEntity;
import in.codifi.ambalal.repository.PaymentTransactionRepository;
import in.codifi.ambalal.service.spec.BasePaymentService;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.api.utilities.StringUtil;

@ApplicationScoped
public class CustomPaymentService implements BasePaymentService {

	@Inject
	PaymentTransactionRepository paymentRepository;

	@Override
	public String getWebHookStatus(JSONObject paymentResponse) {
		try {
			System.out.println(" Payment razorpay WebHook 1----->    " + paymentResponse);
			long amount = 0;
			if (paymentResponse != null && !paymentResponse.isEmpty()) {
				JSONObject payLoadDetails = (JSONObject) paymentResponse.get("payload");
				if (payLoadDetails != null && !payLoadDetails.isEmpty()) {
					JSONObject paymentDetails = (JSONObject) payLoadDetails.get("payment");
					if (paymentDetails != null && !paymentDetails.isEmpty()) {
						JSONObject entityDetails = (JSONObject) paymentDetails.get("entity");
						if (entityDetails != null && !entityDetails.isEmpty()) {
							JSONObject notesDetails = (JSONObject) entityDetails.get("notes");
							if (notesDetails != null && !notesDetails.isEmpty()) {
								String product = (String) notesDetails.get("Product");
								if (StringUtil.isNotNullOrEmpty(product) && StringUtil.isEqual("address", product)) {
									String orderId = (String) entityDetails.get("order_id");
									String userID = (String) entityDetails.get("order_id");
									String paymentId = (String) entityDetails.get("id");
									String status = (String) entityDetails.get("status");
//									boolean statusCaptured = (Boolean) entityDetails.get("captured");
									if (StringUtil.isNotNullOrEmpty(status) && StringUtil.isEqual(status, "captured")) {
//											&& statusCaptured) {
										amount = (Long) entityDetails.get("amount");
										long value = amount / 100;
										JSONObject orderDetails = (JSONObject) payLoadDetails.get("order");
										if (orderDetails != null && !orderDetails.isEmpty()) {
											JSONObject orderEntityDetails = (JSONObject) orderDetails.get("entity");
											if (orderEntityDetails != null && !orderEntityDetails.isEmpty()) {
												String receiptId = (String) orderEntityDetails.get("receipt");
												if (StringUtil.isNotNullOrEmpty(receiptId)
														&& StringUtil.isNotNullOrEmpty(orderId)) {
													PaymentTransactionEntity paymentDTO = paymentRepository
															.findByClientCodeAndRazorpayPaymentId(receiptId, paymentId);
													if (paymentDTO == null) {
														paymentDTO = new PaymentTransactionEntity();
														String clientCode = (String) notesDetails.get("clientID");
														if (clientCode != null) {
															paymentDTO.setClientCode(clientCode);
														}
														paymentDTO.setRazorpayOrderId(orderId);
														paymentDTO.setRazorpayPaymentId(paymentId);
														paymentDTO.setRazorpaySignature(
																EkycConstants.RAZORPAY_WEBHOOK_SIGN);
														paymentDTO.setStatus(EkycConstants.RAZORPAY_STATUS_COMPLETED);
														paymentDTO
																.setAmountPaid(Integer.parseInt(String.valueOf(value)));
														paymentDTO.setAmountDue(0);
														paymentRepository.save(paymentDTO);
														System.out.println(
																"Web hook updated for applicationId - " + receiptId);
													}
												}
											}
										}
									}else {
										System.out.println("the atom  status"+status);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ok";
	}

	@Override
	public Response updateAtomPayment(MultivaluedMap<String, String> formParams) {
	    try {
	        String merchantId = formParams.getFirst("MerchantID");
	        String txnId = formParams.getFirst("MerchantTxnID");
	        String amt = formParams.getFirst("AMT");
	        String status = formParams.getFirst("VERIFIED");
	        String bid = formParams.getFirst("BID");
	        String bankName = formParams.getFirst("BankName");
	        String atomTxnId = formParams.getFirst("AtomTxnId");
	        String discriminator = formParams.getFirst("Discriminator");
	        String surcharge = formParams.getFirst("Surcharge");
	        String cardNumber = formParams.getFirst("CardNumber");
	        String txnDate = formParams.getFirst("TxnDate");
	        String customerAccNo = formParams.getFirst("CustomerAccNo");
	        String clientCode = formParams.getFirst("Clientcode");

	        PaymentTransactionEntity paymentEntity =
	                paymentRepository.findByClientCodeAndRazorpayPaymentId(clientCode, txnId);

	        if (paymentEntity == null) {
	            paymentEntity = new PaymentTransactionEntity();
	            paymentEntity.setClientCode(clientCode);
	            paymentEntity.setTxnId(txnId);
	        }

	        paymentEntity.setMerchantId(merchantId);

	        try {
	            paymentEntity.setAmount(Double.parseDouble(amt));
	        } catch (NumberFormatException e) {
	            return Response.status(Response.Status.BAD_REQUEST)
	                    .entity("Invalid amount format: " + amt).build();
	        }

	        paymentEntity.setStatus(status);
	        paymentEntity.setBankId(bid);
	        paymentEntity.setBankName(bankName);
	        paymentEntity.setAtomTxnId(atomTxnId);
	        paymentEntity.setDiscriminator(discriminator);
	        paymentEntity.setSurcharge(surcharge);
	        paymentEntity.setCardNumber(cardNumber);
	        paymentEntity.setTxnDate(txnDate);
	        paymentEntity.setCustomerAccNo(customerAccNo);

	        PaymentTransactionEntity savedEntity = paymentRepository.save(paymentEntity);

	        return Response.ok(savedEntity).build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                .entity("Failed to update payment").build();
	    }
	}

}
