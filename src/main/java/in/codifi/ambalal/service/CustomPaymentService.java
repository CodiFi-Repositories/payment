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

	
	/**
	 * 
	 * capture the webhook response for webhook
	 *
	 * 23-June-2025
	 * 
	 * @author Vennila
	 */
	
	@Override
	public String getWebHookStatus(JSONObject paymentResponse) {
	    try {
	        System.out.println("Payment Razorpay WebHook -----> " + paymentResponse);

	        if (paymentResponse != null && !paymentResponse.isEmpty()) {
	            String eventType = paymentResponse.get("event") != null ? paymentResponse.get("event").toString() : null;

	            JSONObject payload = (JSONObject) paymentResponse.get("payload");
	            if (payload != null) {
	                JSONObject payment = (JSONObject) payload.get("payment");
	                if (payment != null) {
	                    JSONObject entity = (JSONObject) payment.get("entity");
	                    if (entity != null) {

	                        // notes section
	                        JSONObject notes = (JSONObject) entity.get("notes");
	                        String clientCode = notes != null && notes.get("clientID") != null ? notes.get("clientID").toString() : null;
	                        
	                        String accountNumber = notes != null && notes.get("acc_num") != null ? notes.get("acc_num").toString() : null;
	                        String product = notes != null && notes.get("product") != null ? notes.get("product").toString() : null;

	                        // main fields
	                        String orderId = entity.get("order_id") != null ? entity.get("order_id").toString() : null;
	                        String paymentId = entity.get("id") != null ? entity.get("id").toString() : null;
	                        String status = entity.get("status") != null ? entity.get("status").toString() : null;
	                        long amount = entity.get("amount") != null ? Long.parseLong(entity.get("amount").toString()) : 0L;
	                        long fee = entity.get("fee") != null ? Long.parseLong(entity.get("fee").toString()) : 0L;
	                        long tax = entity.get("tax") != null ? Long.parseLong(entity.get("tax").toString()) : 0L;
	                        long createdAt = entity.get("created_at") != null ? Long.parseLong(entity.get("created_at").toString()) : 0L;
	                        String email = entity.get("email") != null ? entity.get("email").toString() : null;
	                        String contact = entity.get("contact") != null ? entity.get("contact").toString() : null;
	                        String method = entity.get("method") != null ? entity.get("method").toString() : null;
	                        String currency = entity.get("currency") != null ? entity.get("currency").toString() : null;

	                        // upi.vpa
	                        String vpa = null;
	                        if (entity.get("upi") instanceof JSONObject) {
	                            JSONObject upi = (JSONObject) entity.get("upi");
	                            vpa = upi.get("vpa") != null ? upi.get("vpa").toString() : null;
	                        }

	                        // acquirer_data.rrn
	                        String rrn = null;
	                        if (entity.get("acquirer_data") instanceof JSONObject) {
	                            JSONObject acquirerData = (JSONObject) entity.get("acquirer_data");
	                            rrn = acquirerData.get("rrn") != null ? acquirerData.get("rrn").toString() : null;
	                        }

//	                        long value = amount / 100;

	                        if ("captured".equalsIgnoreCase(status) || "failed".equalsIgnoreCase(status)) {

	                            PaymentTransactionEntity paymentDT = paymentRepository.findByClientCodeAndRazorpayPaymentId(clientCode, paymentId);

	                            if (paymentDT == null) {
	                                paymentDT = new PaymentTransactionEntity();
	                                paymentDT.setClientCode(clientCode);
	                                paymentDT.setRazorpayProduct(product);
	                                paymentDT.setRazorpayOrderId(orderId);
	                                paymentDT.setRazorpayPaymentId(paymentId);
	                                paymentDT.setAmountPaid((int) amount/100);
	                                paymentDT.setAmountDue("captured".equalsIgnoreCase(status) ? 0 : (int) amount);
	                                paymentDT.setIsRazorpay(true);
	                                paymentDT.setIsAtom(false);
	                                paymentDT.setRazorpayMethod(method);
	                                paymentDT.setRazorpayVpa(vpa);
	                                paymentDT.setRazorpayRrn(rrn);
	                                paymentDT.setRazorpayEmail(email);
	                                paymentDT.setRazorpayContact(contact);
	                                paymentDT.setRazorpayFee((int) fee);
	                                paymentDT.setRazorpayTax((int) tax);
	                                paymentDT.setRazorpayCurrency(currency);
	                                paymentDT.setRazorpayCreatedAtEpoch(createdAt);
	                                paymentDT.setRazorpaySignature(EkycConstants.RAZORPAY_WEBHOOK_SIGN);
	                                paymentDT.setRazorpayWebhookEvent(eventType);
	                                paymentDT.setRazorpayWebhookRawData(paymentResponse.toJSONString());
	                                paymentDT.setIsRazorpay(true);
	                                paymentDT.setRazorpayAcountNumber(accountNumber);
	                                if ("captured".equalsIgnoreCase(status)) {
	                                    paymentDT.setStatus(EkycConstants.RAZORPAY_STATUS_COMPLETED);
	                                } else {
	                                    paymentDT.setStatus(EkycConstants.RAZORPAY_STATUS_FAILED);
	                                }

	                                paymentRepository.save(paymentDT);
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
	        paymentEntity.setIsAtom(true);
	        PaymentTransactionEntity savedEntity = paymentRepository.save(paymentEntity);

	        return Response.ok(savedEntity).build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                .entity("Failed to update payment").build();
	    }
	}

}
