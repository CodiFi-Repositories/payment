package in.codifi.ambalal.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import in.codifi.ambalal.controller.spec.BasePaymentController;
import in.codifi.ambalal.service.spec.BasePaymentService;

@Path("/payments") // The path here is the base for the whole controller
public class CustomPaymentController implements BasePaymentController {

	@Inject
	BasePaymentService basePaymentService;

	@Override
	public String getWebHookStatus(String webResponse) {
		System.out.println("the webHook is Running");
		System.out.println("the webHook response is " + webResponse);
		return webResponse;
	}

	@Override
	public String testMethod() {
		System.out.println("the test is running");
		return "test";
	}

	@Override
	public Response getWebHookStatus(MultivaluedMap<String, String> formParams) {
	    System.out.println("The webhook is running. Form data received:");

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

	    // Log or process all fields
	    System.out.println("Merchant ID: " + merchantId);
	    System.out.println("Transaction ID: " + txnId);
	    System.out.println("Amount: " + amt);
	    System.out.println("Status: " + status);
	    System.out.println("Bank ID: " + bid);
	    System.out.println("Bank Name: " + bankName);
	    System.out.println("Atom Txn ID: " + atomTxnId);
	    System.out.println("Discriminator: " + discriminator);
	    System.out.println("Surcharge: " + surcharge);
	    System.out.println("Card Number: " + cardNumber);
	    System.out.println("Transaction Date: " + txnDate);
	    System.out.println("Customer Account No: " + customerAccNo);
	    System.out.println("Client Code: " + clientCode);
	    
	    
	    return basePaymentService.updateAtomPayment(formParams);

//	    return Response.ok("{\"message\": \"Callback received successfully\"}").build();
	}

	/**
	 * 
	 * capture the webhook response for webhook
	 * 
	 **/

	@Override
	public String getRazorpayWebHookStatus(String webResponse) {
		JSONObject webHookResponse = (JSONObject) JSONValue.parse(webResponse);
		return basePaymentService.getWebHookStatus(webHookResponse);
	}
	
}
