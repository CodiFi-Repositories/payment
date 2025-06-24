package in.codifi.ambalal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
	 @JsonProperty("name")
    private String name;
	 @JsonProperty("password")
    private String password;
}
