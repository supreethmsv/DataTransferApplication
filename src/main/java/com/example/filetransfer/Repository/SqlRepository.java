package com.example.filetransfer.Repository;
import com.example.filetransfer.Models.SqlInfo;
import org.springframework.stereotype.Repository;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class SqlRepository {
    public boolean save (Stream<String> data, SqlInfo sqlInfo) throws SQLException {
        String connectionUrl =
                "jdbc:sqlserver://"+sqlInfo.getServerName()+".database.windows.net:1433;"
                        + "database="+sqlInfo.getDatabaseName()+";"
                        + "user="+sqlInfo.getServerName()+"@"+sqlInfo.getUserName()+";"
                        + "password="+sqlInfo.getPassword()+";"
                        + "encrypt=true;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";
        ResultSet resultSet = null;
        Connection connection;

        {
            try {
                connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();
                List<String> list = data.collect(Collectors.toList());
                boolean isFirstRowAsHeader = sqlInfo.isFirstRowAsHeader();
                //for(int i=0;i<list.size();i++)  System.out.println(list.get(i));
                String columnList = null;
                String delimiter = null;
                if(sqlInfo.getRowDelimiter().charAt(0) == ',') {
                    delimiter = ",";
                } else {
                    delimiter = "\\|";
                }
                if(isFirstRowAsHeader) {
                    columnList = list.remove(0);
                } else {
                    try {
                        ResultSet columnNameResultSet = statement.executeQuery("SELECT column_name\n" +
                                "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                                "WHERE TABLE_NAME = N'"+sqlInfo.getTableName()+"'");
                        StringBuilder strBuilder = new StringBuilder();
                        while(columnNameResultSet.next()) {
                            strBuilder.append(columnNameResultSet.getString("column_name")).append(",");
                        }
                        columnList = strBuilder.substring(0, strBuilder.length()-1);
                        System.out.println(columnList);
                    } catch (SQLException throwables) {
                        throw throwables;
                    }
                }
                System.out.println("isAutoCreate "+sqlInfo.isAutoCreateTable());
                if(sqlInfo.isAutoCreateTable()) {
                    if(isFirstRowAsHeader) inferSchemaAndCreateTable(list.get(1), statement, delimiter, sqlInfo.getTableName(), columnList);
                    else inferSchemaAndCreateTable(list.get(0), statement, delimiter, sqlInfo.getTableName(), columnList);
                }

                for(String row: list) {
                    //System.out.println(row);

                    String [] columnArray = row.split(delimiter);
                   // System.out.println(sqlInfo.getRowDelimiter().charAt(0));
                    //System.out.println(columnArray[0]);
                    StringBuilder toInsert = new StringBuilder();
                    for(String column: columnArray) {
                        toInsert.append(column).append(',');
                    }
                    String rowToInsert = toInsert.substring(0, toInsert.length()-1);
                    try {
                        //System.out.println("insert into " +sqlInfo.getTableName()+ "("+columnList+") values ("+rowToInsert+")");
                        statement.execute("insert into " +sqlInfo.getTableName()+ "("+columnList+") values ("+rowToInsert+")");
                    } catch (SQLException throwables) {
                        throw throwables;
                    }
                }
            } catch (SQLException throwables) {
                throw throwables;
            }
            return true;
        }
    }

    public void inferSchemaAndCreateTable(String row, Statement statement, String delimiter, String tableName, String columnList) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
                sqlQuery.append("CREATE TABLE "+tableName+"( ");
        String [] columnArray = row.split(delimiter);
        String [] schema = columnList.split(delimiter);
        int index = 0;
        for(String column: columnArray) {
            column = column.replaceAll("\'","");
            sqlQuery.append(schema[index++]);
            System.out.println(column+":"+column.matches("^\\d{4}-\\d{2}-\\d{2}$"));
            if(column.matches(".*[a-zA-Z]+.*")) sqlQuery.append(" varchar(255),");
            else if(column.matches("^\\d{4}-\\d{2}-\\d{2}$")) sqlQuery.append(" Datetime,");
            else if(column.matches("-?\\d+(\\.\\d+)?")) sqlQuery.append(" int,");

        }
        String sqlQueryStmt = sqlQuery.substring(0, sqlQuery.length()-1).concat(")");
        System.out.println(sqlQueryStmt);
        try {
            statement.execute(sqlQueryStmt);
        } catch (SQLException throwables) {
            throw throwables;
        }
    }


}
