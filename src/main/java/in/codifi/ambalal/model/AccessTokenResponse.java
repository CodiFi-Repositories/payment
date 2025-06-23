package in.codifi.ambalal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenResponse {

	   @JsonProperty("Status")
	    private String status;

	    @JsonProperty("AccessToken")
	    private String accessToken;

	    @JsonProperty("Expires")
	    private String expires;

	    @JsonProperty("Message")
	    private String message;

}
