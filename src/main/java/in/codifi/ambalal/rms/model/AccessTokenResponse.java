package in.codifi.ambalal.rms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenResponse {
	 @JsonProperty("type")
	private String type;
	 @JsonProperty("code")
    private String code;
	 @JsonProperty("description")
    private String description;
	 @JsonProperty("result")
    private TokenResult result;
    
    
 // nested class
    @Getter
    @Setter
    public static class TokenResult {

        @JsonProperty("UserID")
        private String userID;

        @JsonProperty("Token")
        private String token;
       
    }
}



