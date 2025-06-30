package in.codifi.ambalal.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import in.codifi.ambalal.entity.logs.AccessLogModel;
import in.codifi.ambalal.entity.logs.RestAccessLogModel;
import io.quarkus.logging.Log;
import in.codifi.ambalal.error.utility.ErrorCodeConstants;
import in.codifi.ambalal.error.utility.ErrorHandling;
import in.codifi.ambalal.error.utility.ErrorMessageConstants;
import in.codifi.ambalal.error.utility.MessageConstants;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.api.utilities.StringUtil;

@ApplicationScoped
public class AccessLogManager {
	@Inject
	@Named("logs")
	DataSource dataSource;
	@Inject
	ErrorHandling errorHandling;
	

	/**
	 * method to insert access logs into data base
	 * 
	 * @author sowmiya
	 * @param accessLogModel
	 */
	public void insertAccessLogsIntoDB(AccessLogModel accLogModel) {
		try {

			Date inTimeDate = new Date();
			Connection connection = null;
			Statement state = null;
			PreparedStatement statement = null;
			String date = new SimpleDateFormat("ddMMYYYY").format(inTimeDate);
			String tableName = "tbl_" + date + "_access_log";
			try {

				connection = dataSource.getConnection();
				state = connection.createStatement();

				String insertQuery = "INSERT INTO " + tableName
						+ "(application_id, uri, method, req_id, req_body, res_body, user_agent, device_ip, content_type, session"
						+ " ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				statement = connection.prepareStatement(insertQuery);
				int paramPos = 1;
				statement.setString(paramPos++, accLogModel.getApplicationId());
				statement.setString(paramPos++, accLogModel.getUri());
				statement.setString(paramPos++, accLogModel.getMethod());
				statement.setString(paramPos++, accLogModel.getReqId());
				statement.setString(paramPos++, accLogModel.getReqBody());
				statement.setString(paramPos++, accLogModel.getResBody());
				statement.setString(paramPos++, accLogModel.getUserAgent());
				statement.setString(paramPos++, accLogModel.getDeviceIp());
				statement.setString(paramPos++, accLogModel.getContentType());
				statement.setString(paramPos++, accLogModel.getSession());
				statement.executeUpdate();

				statement.close();
				state.close();
				connection.close();
			} catch (Exception e) {
				Log.error("Ekyc - insertAccessLog -" + e);
				errorHandling.handleErrors("", "", MessageConstants.MODULE, ErrorCodeConstants.EC012,
						EkycConstants.INTERNAL_ERR, "insertAccessLogsIntoDB", EkycConstants.ACCESSLOG_FILTER,
						e.getMessage(), ErrorMessageConstants.CAPTURE_IN_SINGLESHOT);
			} finally {
				try {
					if (statement != null) {
						statement.close();
					}
					if (state != null) {
						state.close();
					}
					if (connection != null) {
						connection.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
					errorHandling.handleErrors("", "", MessageConstants.MODULE, ErrorCodeConstants.EC012,
							EkycConstants.INTERNAL_ERR, "insertAccessLogsIntoDB", EkycConstants.ACCESS_LOG_MANAGER,
							e.getMessage(), ErrorMessageConstants.CAPTURE_IN_SINGLESHOT);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorHandling.handleErrors("", "", MessageConstants.MODULE, ErrorCodeConstants.EC012,
					EkycConstants.INTERNAL_ERR, "insertAccessLogsIntoDB", EkycConstants.ACCESS_LOG_MANAGER,
					e.getMessage(), ErrorMessageConstants.CAPTURE_IN_SINGLESHOT);
		}
	}

	public void insertRestAccessLogsIntoDB(String applicationId, String Req, String Res, String Method, String Uri) {
		try {
			RestAccessLogModel accRestLogModel = new RestAccessLogModel();
			accRestLogModel.setApplicationId(applicationId);
			accRestLogModel.setReqBody(Req);
			accRestLogModel.setResBody(Res);
			accRestLogModel.setMethod(Method);
			accRestLogModel.setUri(Uri);
			Date inTimeDate = new Date();
			Connection connection = null;
			Statement state = null;
			PreparedStatement statement = null;
			String date = new SimpleDateFormat("ddMMYYYY").format(inTimeDate);
			String tableName = "tbl_" + date + "_rest_access_log";
			try {
				connection = dataSource.getConnection();
				state = connection.createStatement();

				String insertQuery = "INSERT INTO " + tableName + "(application_id, uri, method,req_body, res_body"
						+ " ) VALUES ( ?, ?, ?,?, ?)";

				statement = connection.prepareStatement(insertQuery);
				int paramPos = 1;
				statement.setString(paramPos++, accRestLogModel.getApplicationId());
				statement.setString(paramPos++, accRestLogModel.getUri());
				statement.setString(paramPos++, accRestLogModel.getMethod());
				statement.setString(paramPos++, accRestLogModel.getReqBody());
				statement.setString(paramPos++, accRestLogModel.getResBody());
				statement.executeUpdate();
				statement.close();
				state.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
				errorHandling.handleErrors(StringUtil.isNotNullOrEmpty(applicationId) ? applicationId : "", "",
						MessageConstants.MODULE, ErrorCodeConstants.EC013, EkycConstants.INTERNAL_ERR,
						"insertRestAccessLogsIntoDB", EkycConstants.ACCESSLOG_FILTER, e.getMessage(),
						ErrorMessageConstants.SAVE_REST_LOG);
			} finally {
				try {
					if (statement != null) {
						statement.close();
					}
					if (state != null) {
						state.close();
					}
					if (connection != null) {
						connection.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
					errorHandling.handleErrors(StringUtil.isNotNullOrEmpty(applicationId) ? applicationId : "", "",
							MessageConstants.MODULE, ErrorCodeConstants.EC013, EkycConstants.INTERNAL_ERR,
							"insertRestAccessLogsIntoDB", EkycConstants.ACCESSLOG_FILTER, e.getMessage(),
							ErrorMessageConstants.SAVE_REST_LOG);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorHandling.handleErrors(StringUtil.isNotNullOrEmpty(applicationId) ? applicationId : "", "",
					MessageConstants.MODULE, ErrorCodeConstants.EC013, EkycConstants.INTERNAL_ERR,
					"insertRestAccessLogsIntoDB", EkycConstants.ACCESSLOG_FILTER, e.getMessage(),
					ErrorMessageConstants.SAVE_REST_LOG);
		}

	}
}
