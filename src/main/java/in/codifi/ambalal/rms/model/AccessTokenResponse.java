package in.codifi.ambalal.rms.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenResponse {

	private String type;
    private String code;
    private String description;
    private TokenResult result;
    
    
 // nested class
    @Getter
    @Setter
    public static class TokenResult {
        private String UserID;
        private String Token;
       
    }
}



