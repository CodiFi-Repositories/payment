package in.codifi.ambalal.rms.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenRequest {

	private String userID;
	private String password;
}
