package in.codifi.ambalal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenRequest {

	 
    @JsonProperty("Authorization") // <-- Ensure exact casing
    private String authorization;
}
