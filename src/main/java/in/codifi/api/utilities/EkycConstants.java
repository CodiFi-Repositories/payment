package in.codifi.api.utilities;

public class EkycConstants {

	
	public static final String RAZORPAY_STATUS_COMPLETED = "completed";
	public static final String RAZORPAY_WEBHOOK_SIGN = "From Web hook";
	public static final String RAZORPAY_STATUS_CREATED = "created";
	public static final String RAZORPAY_STATUS_FAILED = "FAILED";
	
	public static final String GLOBE_FALSE = "Globe is not needed.";
	public static final String GLOBE_NOT_USED = "Admin configuration does not allow Globe access due to a false value in the database.";
	
	public static final String TECHEXCEL_FALSE = "TechExcel is not needed.";
	public static final String TECHEXCEL_NOT_USED = "Admin configuration does not allow TechExcel access due to a false value in the database.";

	public static final String RMS_FALSE = "RMS is not needed.";
	public static final String RMS_NOT_USED = "Admin configuration does not allow RMS access due to a false value in the database.";

	
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
	public static final String INVALID_DATE = "Invalid date format,Date must be in yyyy-MM-dd format";

	
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
	
	//Login
	public static final String NO_RECORD_FOUND = "No Record Found";


	
	// Logs DB
		public static final String TABLE_CREATED = "Table Created Successfully";
		public static final String PATH_LOG_TABLE = "/logs/Logtables";
		public static final String PATH_REST_LOG_TABLE = "/logs/RestServiceLogtables";
		public static final String CMR_COPY = "CMR_COPY";
		
	//GLOBE service class
		public static final String GLB_CLASS = "GlobeClass";
		public static final String GLB_TOKEN = "GlobeToken";
		public static final String GLB_UPDATION = "GlobeUpdation";
		public static final String GLB_INQUIRY = "GlobeInquiry";
		
	//RMS SERVICE CLASS
		public static final String RMS_CLASS = "RMSClass";
		public static final String RMS_UPDATION = "RMSUpdation";
		
	//TECHEXCEL CLASS
		public static final String TECHEXCEL_CLASS = "TechExcelClass";
		public static final String TECHEXCEL_TOKEN = "TechExcelToken";
		public static final String TECHEXCEL_UPDATION = "TechExcelUpdation";
		
	//PAYMENT CLASS
		public static final String PAYMENT_CLASS = "PaymentClass";
		public static final String RAZORPAY_PAYMENT = "RazorpayPayment";
		public static final String ATOM_PAYMENT = "AtomPayment";

    //Admin failed transaction CLASS
		public static final String ADMIN_CLASS = "PaymentClass";
		public static final String FETCH_FAILED_TRANSACTIONS = "Fetching Transaction Failed";
		public static final String NO_DATA_FOUND = "No Data Found";
		public static final String DATA_FOUND = "Data fetched from the DB";
		
		
		public static final String FAILED_UPDATE_CONTROL = "Failed to update the admin control in the db ";

		public static final String NO_MATCHED_TRANSACTIONS = "No matching payment transactions found";
		public static final String FETCHED_SUCCESSFULLY="Payment transactions fetched successfully";
		
		public static final String DOWNLOAD_PAYMENT_STATUS="Payment transactions details are downloaded";
		
		public static final String DATA_SAVED_SUCCESSFULLY="Data saved in the DB";
		public static final String DATA_SAVE_FAILED = "Failed to save data in the DB";
		public static final String DB_SAVE_EXCEPTION = "An error occurred while saving data to the database";
		public static final String NO_MATCHED_USER = "The given user id is not found";
		
	//Admin login
		public static final String ADMIN_LOGIN_CLASS = "AdminLoginClass";

		public static final String ADMIN_LOGIN_SUCCESS = "Admin login completed successfully";
		public static final String ADMIN_LOGIN_FAILED = "Invalid email ID or password";
		public static final String ADMIN_LOGIN_INACTIVE = "Admin account is inactive";
		public static final String ADMIN_LOGIN_MISSING = "Login credentials are missing";
		public static final String ADMIN_LOGIN_EXCEPTION = "Unexpected error during admin login";

		public static final String EXCEPTION_OCCURRED ="Unexpected error during Fetching data in admin control table";
		public static final String CREATE_ADMIN_USER = "Admin user created successfully";
		public static final String USER_CREATION_FAILED = "Failed to create admin user";
		public static final String LOGIN_FAILED = "Admin Login failed";
		public static final String UPDATE_SUCCESSFULLY ="All updates processed successfully";


	

		

		
		

}
