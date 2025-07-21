package in.codifi.ambalal.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "admin_login_credentials")
public class AdminLoginEntity extends CommonEntity implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	
	@Column(name = "emailId")
	private String emailId;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "passowrd")
	private String password;

	@Column(name = "status")
	private String status;
	
	@Column(name = "role")
	private String role;

}
