package in.codifi.ambalal.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int stat;
	private String page;
	private String message;
	private String errorCode;
	private String reason;
	private Object result;
	private Object Address_response;
	private Object rejectionUser;

}
