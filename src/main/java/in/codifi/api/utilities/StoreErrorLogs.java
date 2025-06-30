package in.codifi.api.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import in.codifi.ambalal.error.utility.ErrorCodeConstants;
import in.codifi.ambalal.error.utility.ErrorHandling;
import in.codifi.ambalal.error.utility.ErrorMessageConstants;
import in.codifi.ambalal.rms.model.ErrorLogModel;
import in.codifi.ambalal.error.utility.MessageConstants;

@ApplicationScoped
public class StoreErrorLogs {

	@Inject
	@Named("logs")
	DataSource dataSource;
	@Inject
	ErrorHandling errorHandling;

	public void insertErrorLogsIntoDB(ErrorLogModel errorLogEntity) {
		Connection connection = null;
		PreparedStatement statement = null;
		Statement state = null;

		try {
			// Get current date for table name suffix
			Date inTimeDate = new Date();
			String date = new SimpleDateFormat("ddMMYYYY").format(inTimeDate);
			String tableName = "tbl_" + date + "_error_log";

			// Get database connection
			connection = dataSource.getConnection();
			state = connection.createStatement();

			// Insert query matching your table structure
			String insertQuery = "INSERT INTO " + tableName
					+ " (application_id,api_endpoint,module,error_code,type_of_api , method_name, error_message,class_name) VALUES (?,?,?,?,?,?,?,?)";

			// Prepare statement
			statement = connection.prepareStatement(insertQuery);

			// Set values from ErrorLogEntity
			statement.setString(1, errorLogEntity.getClientId());
			statement.setString(2, errorLogEntity.getEndPoint());
			statement.setString(3, errorLogEntity.getModule());
			statement.setString(4, errorLogEntity.getErrorCode());
			statement.setString(5, errorLogEntity.getApiType());
			statement.setString(6, errorLogEntity.getMethodName());
			statement.setString(7, errorLogEntity.getReason());
			statement.setString(8, errorLogEntity.getClassName());

			// Execute the query
			statement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			errorHandling.handleErrors("", "", MessageConstants.MODULE, ErrorCodeConstants.EC014,
					EkycConstants.INTERNAL_ERR, "createErrorLogsTable", EkycConstants.STORE_ERROR_LOG, e.getMessage(),
					ErrorMessageConstants.STORE_ERROR_LOG);
		} finally {
			// Close resources in the finally block
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
				errorHandling.handleErrors("", "", MessageConstants.MODULE, ErrorCodeConstants.EC014,
						EkycConstants.INTERNAL_ERR, "createErrorLogsTable", EkycConstants.STORE_ERROR_LOG,
						e.getMessage(), ErrorMessageConstants.STORE_ERROR_LOG);
			}
		}
	}

}
