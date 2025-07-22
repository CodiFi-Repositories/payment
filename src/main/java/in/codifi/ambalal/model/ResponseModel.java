package in.codifi.ambalal.model;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ApplicationScoped
public class ResponseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int stat;
	//private String status;
	private String page;
	private String message;
	private String errorCode;
	private String reason;
	private Object result;
	private String token;

	
	private Object Address_response;
	private Object rejectionUser;
	
	private String authToken;


}
