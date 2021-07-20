package com.example.filetransfer.Repository;
import com.example.filetransfer.Models.SqlInfo;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class SqlRepository {
    /* Saves the data file stream into the SQL table
     * Returns - true or false
     * throws - SQL Exception
     */
    public boolean save(Stream<String> data, SqlInfo sqlInfo) throws SQLException {
        //Establishing the connection to Azure SQL DB by getting details from the front end
        String connectionUrl =
                "jdbc:sqlserver://" + sqlInfo.getServerName() + ".database.windows.net:1433;"
                        + "database=" + sqlInfo.getDatabaseName() + ";"
                        + "user=" + sqlInfo.getServerName() + "@" + sqlInfo.getUserName() + ";"
                        + "password=" + sqlInfo.getPassword() + ";"
                        + "encrypt=true;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";
        ResultSet resultSet = null;
        Connection connection;

        //Create connection and SQL statement to run queries later
        connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();
        //Data is read from the file and stored as a Stream, assigning to list for easy traversal
        List<String> list = data.collect(Collectors.toList());
        boolean isFirstRowAsHeader = sqlInfo.isFirstRowAsHeader();
        boolean isAutoCreateTable = sqlInfo.isAutoCreateTable();
        String columnList = null;
        String delimiter = null;
        //Setting the row delimiter as a regex, depending upon the user input to be used to split the input file
        if (sqlInfo.getRowDelimiter().charAt(0) == ',') {
            delimiter = ",";
        } else {
            delimiter = "\\|";
        }
        //If first row is header, remove that row from the list and use the same to be column names in SQL
        if (isFirstRowAsHeader) {
            columnList = list.remove(0);
            //if auto create table option is enabled, infer the schema from the data and create table
            if(isAutoCreateTable) {
                inferSchemaAndCreateTable(list.get(1), statement, delimiter, sqlInfo.getTableName(), columnList);
            }
        } else {
            //if auto create table option is enabled, infer the schema from the data and create table
            if(isAutoCreateTable) {
                inferSchemaAndCreateTable(list.get(0), statement, delimiter, sqlInfo.getTableName(), columnList);
            }

            //Get the schema from the existing table and generate the row header
            ResultSet columnNameResultSet = statement.executeQuery("SELECT column_name\n" +
                    "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                    "WHERE TABLE_NAME = N'" + sqlInfo.getTableName() + "'");
            if(!columnNameResultSet.next()) throw new SQLException("Table " +sqlInfo.getTableName()+" not found");
            StringBuilder strBuilder = new StringBuilder();
            while (columnNameResultSet.next()) {
                strBuilder.append(columnNameResultSet.getString("column_name")).append(",");
            }
            if(strBuilder.length() != 0) columnList = strBuilder.substring(0, strBuilder.length() - 1);
        }

        //Traverse the list array and get the data split based on the delimiter
        for (String row : list) {
            String[] columnArray = row.split(delimiter);
            StringBuilder toInsert = new StringBuilder();
            for (String column : columnArray) {
                toInsert.append(column).append(',');
            }
            //No need of the last ',' character, so using substring function
            String rowToInsert = toInsert.substring(0, toInsert.length() - 1);
            //Insert the data into the table
            statement.execute("insert into " + sqlInfo.getTableName() + "(" + columnList + ") values (" + rowToInsert + ")");
        }
        return true;

    }

    /* Infers the schema and create the table
     * throws - SQL Exception
     */
    public void inferSchemaAndCreateTable(String row, Statement statement, String delimiter, String tableName, String columnList) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
        String[] schema = null;
        sqlQuery.append("CREATE TABLE " + tableName + "( ");
        if(columnList != null) {
            schema = columnList.split(delimiter);
        }
        String[] columnArray = row.split(delimiter);

        int index = 0;
        for (String column : columnArray) {
            column = column.replaceAll("\'", "");
            //if auto-create table option and first row as header is true, append column_i, where i is the index,
            // else append the actual column name to the create table syntax
            if(columnList == null) sqlQuery.append("column_").append(index++);
            else sqlQuery.append(schema[index++]);
            //for POC, generating schema for only Integer, Date and Varchar data types
            if (column.matches(".*[a-zA-Z]+.*")) sqlQuery.append(" varchar(255),");
            else if (column.matches("^\\d{4}-\\d{2}-\\d{2}$")) sqlQuery.append(" Datetime,");
            else if (column.matches("-?\\d+(\\.\\d+)?")) sqlQuery.append(" int,");
        }
        //No need of the last ',' character, so using substring function
        String sqlQueryStmt = sqlQuery.substring(0, sqlQuery.length() - 1).concat(")");
        statement.execute(sqlQueryStmt);
    }
}
