package in.codifi.ambalal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechLoginResponse {

    @JsonProperty("Success")
    private String success;

    @JsonProperty("Success Description")
    private String successDescription;

    @JsonProperty("Error Code")
    private String errorCode;

    @JsonProperty("Error Description")
    private Object errorDescription;

   
}
