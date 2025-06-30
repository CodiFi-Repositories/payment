package in.codifi.api.utilities;

public class EkycConstants {

	
	public static final String RAZORPAY_STATUS_COMPLETED = "completed";
	public static final String RAZORPAY_WEBHOOK_SIGN = "From Web hook";
	public static final String RAZORPAY_STATUS_CREATED = "created";
	public static final String RAZORPAY_STATUS_FAILED = "FAILED";
	
	
	
	public static final String EMAIL_REGEX = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
	public static final String PAN_REGEX = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
	public static final String MOBILE_REGEX = "^[6-9]\\d{9}$";
	public static final int FAILED_STATUS = 0;
	public static final int SUCCESS_STATUS = 1;
	public static final String FAILED_MSG = "Failed";
	public static final String SUCCESS_MSG = "Success";
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	public static final String ERROR_HANDLING = "ErrorHandling";
	public static final String INTERNAL_ERR = "Internal";
	
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String IVR_EMAIL_KEY_1 = "EMAIL";
	public static final String CONSTANT_TEXT_HTML = "text/html";

	
	// Mail Constants
	public static final String CONST_MAIL_HOST = "mail.smtp.host";
	public static final String CONST_MAIL_USER = "mail.smtp.user";
	public static final String CONST_MAIL_PORT = "mail.smtp.port";
	public static final String CONST_MAIL_SOC_FAC_PORT = "mail.smtp.socketFactory.port";
	public static final String CONST_MAIL_AUTH = "mail.smtp.auth";
	public static final String CONST_MAIL_DEBUG = "mail.smtp.debug";
	public static final String CONST_MAIL_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
	public static final String CONST_MAIL_SSL_PROTOCOLS = "mail.smtp.ssl.protocols";
	public static final String CONST_MAIL_TLS_V2 = "TLSv1.2";
	
	public static final String LOG_REPO = "LogRepository";
	public static final String STORE_ERROR_LOG = "StoreErrorLogs";
	public static final String COMMON_MAIl = "CommonMail";
	public static final String COMMON_METHOD = "CommonMethods";
	public static final String ACCESSLOG_FILTER = "AccessLogFilter";
	public static final String ACCESS_LOG_MANAGER = "AccessLogManager";

	
	// Logs DB
		public static final String TABLE_CREATED = "Table Created Successfully";
		public static final String PATH_LOG_TABLE = "/logs/Logtables";
		public static final String PATH_REST_LOG_TABLE = "/logs/RestServiceLogtables";
		public static final String CMR_COPY = "CMR_COPY";
		
	//GLOBE service class
		public static final String GLB_CLASS = "GLOBE_CLASS";
		public static final String GLB_TOKEN = "GLOBE_TOKEN";
		public static final String GLB_UPDATION = "GLOBE_UPDATION";
		public static final String GLB_INQUIRY = "GLOBE_INQUIRY";
		
	//RMS SERVICE CLASS
		public static final String RMS_CLASS = "RMS_CLASS";
		public static final String RMS_UPDATION = "GLOBE_UPDATION";
		
	//TECHEXCEL CLASS
		public static final String TECHEXCEL_CLASS = "TECHEXCEL_CLASS";
		public static final String TECHEXCEL_TOKEN = "TECHEXCEL_TOKEN";
		public static final String TECHEXCEL_UPDATION = "TECHEXCEL_UPDATION";
		
	//PAYMENT CLASS
		public static final String PAYMENT_CLASS = "PAYMENT_CLASS";
		public static final String RAZORPAY_PAYMENT = "RAZORPAY_PAYMENT";
		public static final String ATOM_PAYMENT = "ATOM_PAYMENT";

		
		
		

}
