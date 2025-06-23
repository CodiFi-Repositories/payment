package in.codifi.ambalal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusInquiryRequest {

	
	 @JsonProperty("AccessToken") // <-- Ensure exact casing
	  private String AccessToken;
	    
	    // Note: Fixing space typo in msgId key
	 @JsonProperty("msgId") // <-- Ensure exact casing
	    private String msgId;
	 @JsonProperty("dataFormat") // <-- Ensure exact casing
	    private String dataFormat;
}
