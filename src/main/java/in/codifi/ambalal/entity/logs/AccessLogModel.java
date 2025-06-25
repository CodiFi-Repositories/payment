package in.codifi.ambalal.entity.logs;

import java.io.Serializable;
import java.sql.Timestamp;

import in.codifi.ambalal.entity.CommonEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessLogModel extends CommonEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String applicationId;
	private String uri;
	private String method;
	private String reqId;
	private Timestamp inTime;
	private Timestamp outTime;
	private String domain;
	private String reqBody;
	private String resBody;
	private String userAgent;
	private String deviceIp;
	private String contentType;
	private String session;

}
