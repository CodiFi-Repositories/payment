package in.codifi.ambalal.service.spec;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

public interface BasePaymentService {

	
	/**
	 * 
	 * capture the webhook response for webhook
	 *
	 * 06-May-2025
	 * 
	 * @author Vennila
	 */

	String getWebHookStatus(JSONObject webHookResponse);

	/**
	 * 
	 * capture the webhook response for webhook
	 *
	 * 21-May-2025
	 * 
	 * @author Vennila
	 */

	
	Response updateAtomPayment(MultivaluedMap<String, String> formParams);
}
