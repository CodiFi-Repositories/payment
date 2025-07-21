package in.codifi.ambalal.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

//import com.mysql.cj.protocol.x.MessageConstants;

import in.codifi.ambalal.model.ResponseModel;
import in.codifi.ambalal.repository.LogRepository;
import in.codifi.ambalal.service.spec.ILogService;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.ambalal.error.utility.ErrorCodeConstants;
import in.codifi.ambalal.error.utility.ErrorHandling;
import in.codifi.ambalal.error.utility.ErrorMessageConstants;
import in.codifi.ambalal.error.utility.MessageConstants;
import in.codifi.api.utilities.CommonMail;
import in.codifi.api.utilities.CommonMethods;
import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class LogService implements ILogService {

	@Inject
	LogRepository repository;
	@Inject
	CommonMail commonMail;
	
	@Inject
	ResponseModel responseModel;
	
	@Inject 
	ErrorHandling errorHandling;

	@Scheduled(cron = "0 0 1 ? * MON") // Run every Monday at 1:00 AM
	public void checkRestAccessLogTableRun() {
		checkRestAccessLogTable();
	}

	@Scheduled(cron = "0 0 1 ? * MON") // Run every Monday at 1:00 AM
	public void checkRestServiceAccessLogTableRun() {
		checkRestServiceAccessLogTable();
	}

	@Scheduled(cron = "0 0 1 ? * MON") // Run every Monday at 1:00 AM
	public void checkErrorLogTableRun() {
		createErrorLogsTable();
	}

	/**
	 * method to check rest access log table
	 * 
	 */
	@Override
	public ResponseModel checkRestAccessLogTable() {
		//ResponseModel responseModel = new ResponseModel();
		try {
			/** to get total number of table names from specific database **/
			List<String> existingTable = repository.getExistingTables();
			List<String> tableToCreate = new ArrayList<>();
			List<String> durcateToCreate = new ArrayList<>();
			LocalDate currentDate = LocalDate.now();
			String tableName = "";
			for (int i = 0; i <= 7; i++) {
				LocalDate local = currentDate.plusDays(i);
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMYYYY");
				String formattedDate = dateTimeFormatter.format(local);
				tableName = "tbl_" + formattedDate + "_access_log";
				if (!existingTable.contains(tableName)) {
					tableToCreate.add(tableName);
				}

			}

			for (int i = 0; i <= 7; i++) {
				LocalDate local = currentDate.minusDays(i);
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMYYYY");
				String formattedDate = dateTimeFormatter.format(local);
				tableName = "tbl_" + formattedDate + "_access_log";
				System.out.println("the  tableName" + tableName);
				durcateToCreate.add(tableName);
			}
			repository.backUpExistingTables(durcateToCreate);
			/** if table not exist from database to create the tables **/
			repository.createTables(tableToCreate);
			responseModel.setStat(EkycConstants.SUCCESS_STATUS);
			responseModel.setMessage(EkycConstants.TABLE_CREATED);
			return responseModel;

		} catch (Exception e) {
			e.printStackTrace();
			errorHandling.handleErrors("", "/logs/RestServiceLogtables", MessageConstants.MODULE,
					ErrorCodeConstants.EC010, EkycConstants.INTERNAL_ERR, "checkRestAccessLogTable",
					EkycConstants.LOG_REPO, e.getMessage(), ErrorMessageConstants.CREATE_REST_LOG);
		}
//		responseModel = commonMethods.constructFailedMsg(MessageConstants.FAILED);
		return responseModel;
	}

	@Override
	public ResponseModel checkRestServiceAccessLogTable() {
		//ResponseModel response_Model = new ResponseModel();
		try {
			/** to get total number of table names from specific database **/
			List<String> existingTable = repository.getExistingTables();
			List<String> tableToCreate = new ArrayList<>();
			List<String> durcateToCreate = new ArrayList<>();
			LocalDate currentDate = LocalDate.now();
			String tableName = "";
			for (int i = 0; i <= 7; i++) {
				LocalDate local = currentDate.plusDays(i);
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMYYYY");
				String formattedDate = dateTimeFormatter.format(local);
				tableName = "tbl_" + formattedDate + "_rest_access_log";
				if (!existingTable.contains(tableName)) {
					tableToCreate.add(tableName);
				}

			}
			for (int i = 0; i <= 7; i++) {
				LocalDate local = currentDate.minusDays(i);
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMYYYY");
				String formattedDate = dateTimeFormatter.format(local);
				tableName = "tbl_" + formattedDate + "_rest_access_log";
				System.out.println("the  tableName" + tableName);
				durcateToCreate.add(tableName);
			}
//			repository.backUprestexitingTables(durcateToCreate);
			/** if table not exist from database to create the tables **/
			repository.createRestTable(tableToCreate);
			responseModel.setStat(EkycConstants.SUCCESS_STATUS);
			responseModel.setMessage(EkycConstants.TABLE_CREATED);
			return responseModel;

		} catch (Exception e) {
			e.printStackTrace();
			errorHandling.handleErrors("", "/logs/RestServiceLogtables", MessageConstants.MODULE,
					ErrorCodeConstants.EC010, EkycConstants.INTERNAL_ERR, "checkRestServiceAccessLogTable",
					EkycConstants.LOG_REPO, e.getMessage(), ErrorMessageConstants.CREATE_REST_LOG);
		}
		return responseModel;
	}

	@Override
	public ResponseModel createErrorLogsTable() {
		//ResponseModel responseModel = new ResponseModel();
		try {
			/** to get total number of table names from specific database **/
			List<String> existingTable = repository.getExistingTables();
			List<String> tableToCreate = new ArrayList<>();
			List<String> durcateToCreate = new ArrayList<>();
			LocalDate currentDate = LocalDate.now();
			String tableName = "";
			for (int i = 0; i <= 7; i++) {
				LocalDate local = currentDate.plusDays(i);
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMYYYY");
				String formattedDate = dateTimeFormatter.format(local);
				tableName = "tbl_" + formattedDate + "_error_log";
				if (!existingTable.contains(tableName)) {
					tableToCreate.add(tableName);
				}

			}
			for (int i = 0; i <= 7; i++) {
				LocalDate local = currentDate.minusDays(i);
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMYYYY");
				String formattedDate = dateTimeFormatter.format(local);
				tableName = "tbl_" + formattedDate + "_error_log";
				System.out.println("the  tableName" + tableName);
				durcateToCreate.add(tableName);
			}
			repository.backUprestexitingTables(durcateToCreate);
			/** if table not exist from database to create the tables **/
			repository.createErrorTable(tableToCreate);
			responseModel.setStat(EkycConstants.SUCCESS_STATUS);
			responseModel.setMessage(EkycConstants.TABLE_CREATED);
			return responseModel;

		} catch (Exception e) {
			e.printStackTrace();
			 errorHandling.handleErrors("", "/logs/createErrorLogsTable", MessageConstants.MODULE,
					ErrorCodeConstants.EC010, EkycConstants.INTERNAL_ERR, "createErrorLogsTable",
					EkycConstants.LOG_REPO, e.getMessage(), ErrorMessageConstants.CREATE_ERROR_LOG);
		}
//		responseModel = commonMethods.constructFailedMsg(MessageConstants.FAILED);
		return responseModel;
	}
	


//	@Override
//    public String sendMails(List<String> mailIds, String subject, String message) {
//        return commonMail.sendMail(mailIds, subject, message, false);
//    }
	
}
