package in.codifi.ambalal.rms.model;


import java.io.Serializable;
import java.sql.Timestamp;

import in.codifi.ambalal.entity.CommonEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorLogModel extends CommonEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String clientId;
	private String className;
	private String methodName;
	private String reason;
	private Timestamp inTime;
	private String errorCode;
	private String apiType;
	private String endPoint;
	private String module;
}
