package in.codifi.ambalal.repository;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import com.mysql.cj.protocol.x.MessageConstants;

import in.codifi.ambalal.config.ApplicationProperties;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.kyc.error.utility.ErrorCodeConstants;
import in.codifi.kyc.error.utility.ErrorHandling;
import in.codifi.kyc.error.utility.ErrorMessageConstants;

@ApplicationScoped
public class LogRepository {

	@Named("logs")
	@Inject
	DataSource dataSource;
	@Inject
	ApplicationProperties properties;
	

	/*
	 * method to get specific database total number of table names
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	public List<String> getExistingTables() {
		List<String> tableNames = new ArrayList<>();
		Connection connection = null;
		DatabaseMetaData metaData = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			metaData = connection.getMetaData();
			String[] tableTypes = { "TABLE" };
			resultSet = metaData.getTables(properties.getLogDBName(), null, "%", tableTypes);
			while (resultSet.next()) {
				String tableName = resultSet.getString("TABLE_NAME");
				tableNames.add(tableName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
		return tableNames;
	}

	/*
	 * method to create a table from database
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	public void createTables(List<String> tableToCreate) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String databaseName = properties.getLogDBName();
			for (String tableName : tableToCreate) {
				String sql = "CREATE TABLE " + databaseName + "." + tableName
						+ " (id int AUTO_INCREMENT  PRIMARY KEY, application_id VARCHAR(150),"
						+ "uri text,method varchar(150),req_id VARCHAR(150),"
						+ "req_body longtext,res_body longtext,device_ip varchar(150),"
						+ " createdOn TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
						+ " updatedOn TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
						+ " inTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
						+ " outTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
						+ "user_agent longtext,content_type varchar(200), domain varchar(150), session mediumtext)";
				statement.executeUpdate(sql);
			}
		} catch (Exception e) {
			
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}

	}

	public void createRestTable(List<String> tableToCreate) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String databaseName = properties.getLogDBName();
			for (String tableName : tableToCreate) {
				String sql = "CREATE TABLE " + databaseName + "." + tableName
						+ " (id int AUTO_INCREMENT PRIMARY KEY, application_id VARCHAR(150),"
						+ " uri text, method varchar(150)," + " req_body longtext, res_body longtext,"
						+ " createdOn TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
						+ " updatedOn TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
						+ " inTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
						+ " outTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
						+ " domain varchar(150), activeStatus BOOLEAN)";

				statement.executeUpdate(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
//			errorHandling.handleErrors("", "/logs/RestServiceLogtables", MessageConstants.MODULE,
//					ErrorCodeConstants.EKEC168, EkycConstants.INTERNAL_ERR, "createRestTable", EkycConstants.LOG_REPO,
//					e.getMessage(), ErrorMessageConstants.CREATE_REST_LOG);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
//				errorHandling.handleErrors("", "/logs/RestServiceLogtables", MessageConstants.MODULE,
//						ErrorCodeConstants.EKEC168, EkycConstants.INTERNAL_ERR, "createRestTable",
//						EkycConstants.LOG_REPO, e.getMessage(), ErrorMessageConstants.CREATE_REST_LOG);
			}
		}

	}

	public void backUpExistingTables(List<String> tablesToBackup) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String databaseName = properties.getLogDBName();
			for (String tableName : tablesToBackup) {
				// Check if the table exists
				if (!tableExists(databaseName, tableName, connection)) {
					System.err.println("Table '" + tableName + "' doesn't exist. Skipping backup.");
					continue;
				}

				// SQL query to select all data from the table
				String selectDataQuery = "SELECT * FROM " + databaseName + "." + tableName;

				// Execute query
				ResultSet resultSet = statement.executeQuery(selectDataQuery);

				// Check if result set is empty
				if (!resultSet.next()) {
//	                System.out.println("Table '" + tableName + "' is empty. Skipping backup.");
					continue;
				}

				// Create file path for SQL backup
				String filePath = properties.getFileBasePath() + "logsBackUp\\" + tableName + ".sql";
//	            System.out.println("Exporting table '" + tableName + "' data to " + filePath);
				Path sqlFilePath = Paths.get(filePath);

				// Create directories if they don't exist
				Files.createDirectories(sqlFilePath.getParent());

				// Write SQL create table statement to file
				try (FileWriter writer = new FileWriter(sqlFilePath.toFile())) {
					writer.write(generateCreateTableQuery(tableName, resultSet.getMetaData()) + "\n");

					// Write SQL insert statements to file
					do {
						writer.write(generateInsertStatement(tableName, resultSet) + "\n");
					} while (resultSet.next());
				}
//	            System.out.println("SQL data exported successfully for table " + tableName + " to " + filePath);

				// Drop the table after backup
				// dropTable(databaseName, tableName, connection);
//	            System.out.println("Table '" + tableName + "' dropped successfully after backup.");
			}
		} catch (Exception e) {
			e.printStackTrace();
//			errorHandling.handleErrors("", "/logs/Logtables", MessageConstants.MODULE, ErrorCodeConstants.EKEC168,
//					EkycConstants.INTERNAL_ERR, "backUpExistingTables", EkycConstants.LOG_REPO, e.getMessage(),
//					ErrorMessageConstants.CREATE_ACCESS_LOG);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
//				errorHandling.handleErrors("", "/logs/Logtables", MessageConstants.MODULE, ErrorCodeConstants.EKEC168,
//						EkycConstants.INTERNAL_ERR, "backUpExistingTables", EkycConstants.LOG_REPO, e.getMessage(),
//						ErrorMessageConstants.CREATE_ACCESS_LOG);
			}
		}
	}

	private String generateCreateTableQuery(String tableName, ResultSetMetaData metaData) throws SQLException {
		StringBuilder createTableQuery = new StringBuilder("CREATE TABLE " + tableName + " (");
		int columnCount = metaData.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			String columnName = metaData.getColumnName(i);
			String columnType = getColumnSQLType(metaData.getColumnType(i), metaData.getColumnTypeName(i));
			createTableQuery.append(columnName).append(" ").append(columnType);

			// Include additional column attributes if needed
			// Example: createTableQuery.append(" ").append("NOT NULL");

			if (i < columnCount) {
				createTableQuery.append(", ");
			}
		}
		// Add primary key constraint if applicable
		// Example: createTableQuery.append(", PRIMARY KEY (id)");

		createTableQuery.append(");");
		return createTableQuery.toString();
	}

	private String getColumnSQLType(int columnType, String columnTypeName) {
		// Handle specific column types and convert them to SQL types as needed
		switch (columnType) {
			case Types.VARCHAR:
				return "VARCHAR(255)"; // Adjust length as needed
			case Types.TIMESTAMP:
				return "TIMESTAMP";
			// Add more cases as needed for other column types
			default:
				return columnTypeName;
		}
	}

	private String generateInsertStatement(String tableName, ResultSet resultSet) throws SQLException {
		StringBuilder insertStatement = new StringBuilder();
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columnCount = metaData.getColumnCount();

		// Loop through each row in the ResultSet
		while (resultSet.next()) {
			// Start building INSERT INTO statement for each row
			insertStatement.append("INSERT INTO ").append(tableName).append(" VALUES (");

			// Loop through each column in the row
			for (int i = 1; i <= columnCount; i++) {
				// Retrieve column value
				Object columnValue = resultSet.getObject(i);

				// Append appropriate representation of column value
				if (columnValue != null) {
					if (columnValue instanceof String || columnValue instanceof Timestamp) {
						// Enclose string and timestamp values in single quotes
						insertStatement.append("'").append(escapeString(columnValue.toString())).append("'");
					} else {
						// Other data types can be appended directly
						insertStatement.append(columnValue.toString());
					}
				} else {
					// Handle NULL values
					insertStatement.append("NULL");
				}

				// Add comma if not the last column
				if (i < columnCount) {
					insertStatement.append(", ");
				}
			}

			// End the INSERT INTO statement for the current row
			insertStatement.append(");\n");
		}

		return insertStatement.toString();
	}

	private String escapeString(String value) {
		// Escape single quotes in string values
		return value.replace("'", "''");
	}

//
//
//
//
//
//
//	private void dropTable(String databaseName, String tableName, Connection connection) throws SQLException {
//	    String dropTableQuery = "DROP TABLE " + databaseName + "." + tableName;
//	    try (Statement dropStatement = connection.createStatement()) {
//	        dropStatement.executeUpdate(dropTableQuery);
//	    }
//	}
//
//
	private boolean tableExists(String databaseName, String tableName, Connection connection) throws SQLException {
		try (Statement stmt = connection.createStatement()) {
			ResultSet rs = stmt.executeQuery("SHOW TABLES IN " + databaseName + " LIKE '" + tableName + "'");
			return rs.next();
		}
	}

	public void backUprestexitingTables(List<String> tablesToBackup) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String databaseName = properties.getLogDBName();
			for (String tableName : tablesToBackup) {
				// Check if the table exists
				if (!tableExists(databaseName, tableName, connection)) {
					System.err.println("Table '" + tableName + "' doesn't exist. Skipping backup.");
					continue;
				}

				// SQL query to select all data from the table
				String selectDataQuery = "SELECT * FROM " + databaseName + "." + tableName;

				// Execute query
				ResultSet resultSet = statement.executeQuery(selectDataQuery);

				// Check if result set is empty
				if (!resultSet.next()) {
//	                System.out.println("Table '" + tableName + "' is empty. Skipping backup.");
					continue;
				}

				// Create file path for SQL backup
				String filePath = properties.getFileBasePath() + "restLogsBackUp\\" + tableName + ".sql";
//	            System.out.println("Exporting table '" + tableName + "' data to " + filePath);
				Path sqlFilePath = Paths.get(filePath);

				// Create directories if they don't exist
				Files.createDirectories(sqlFilePath.getParent());

				// Write SQL create table statement to file
				try (FileWriter writer = new FileWriter(sqlFilePath.toFile())) {
					writer.write(generateCreateTableQuery(tableName, resultSet.getMetaData()) + "\n");

					// Write SQL insert statements to file
					do {
						writer.write(generateInsertStatement(tableName, resultSet) + "\n");
					} while (resultSet.next());
				}
//	            System.out.println("SQL data exported successfully for table " + tableName + " to " + filePath);

				// Drop the table after backup
				// dropTable(databaseName, tableName, connection);
				// System.out.println("Table '" + tableName + "' dropped successfully after
				// backup.");
			}
		} catch (Exception e) {
			e.printStackTrace();
//			errorHandling.handleErrors("", "/logs/RestServiceLogtables", MessageConstants.MODULE,
//					ErrorCodeConstants.EKEC168, EkycConstants.INTERNAL_ERR, "backUprestexitingTables",
//					EkycConstants.LOG_REPO, e.getMessage(), ErrorMessageConstants.CREATE_REST_LOG);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
//				errorHandling.handleErrors("", "/logs/RestServiceLogtables", MessageConstants.MODULE,
//						ErrorCodeConstants.EKEC168, EkycConstants.INTERNAL_ERR, "backUprestexitingTables",
//						EkycConstants.LOG_REPO, e.getMessage(), ErrorMessageConstants.CREATE_REST_LOG);
			}
		}
	}

	public void createErrorTable(List<String> tableToCreate) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			String databaseName = properties.getLogDBName();
			for (String tableName : tableToCreate) {
				String sql = "CREATE TABLE " + databaseName + "." + tableName
						+ " (id BIGINT AUTO_INCREMENT PRIMARY KEY, " + "application_id VARCHAR(150), "
						+ " api_endpoint VARCHAR(150), " + "module VARCHAR(150), " + "error_code VARCHAR(150), "
						+ "type_of_api VARCHAR(150), " + "method_name VARCHAR(150), " + "class_name VARCHAR(150), "
						+ " createdOn TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
						+ " updatedOn TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
						+ "error_message TEXT)";
				statement.executeUpdate(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
//			errorHandling.handleErrors("", "/logs/createErrorLogsTable", MessageConstants.MODULE,
//					ErrorCodeConstants.EKEC168, EkycConstants.INTERNAL_ERR, "createErrorTable", EkycConstants.LOG_REPO,
//					e.getMessage(), ErrorMessageConstants.CREATE_ERROR_LOG);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
//				errorHandling.handleErrors("", "/logs/createErrorLogsTable", MessageConstants.MODULE,
//						ErrorCodeConstants.EKEC168, EkycConstants.INTERNAL_ERR, "createErrorTable",
//						EkycConstants.LOG_REPO, e.getMessage(), ErrorMessageConstants.CREATE_ERROR_LOG);
			}
		}
	}
}
