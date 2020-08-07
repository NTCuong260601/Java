package Controller;

import java.sql.Connection;

public interface BaseDAO {

    public Connection getConnection();

    public String getTableName(String suggestions);

    public String[] getColumnName(String tableName);

    public String getCondition(String field, String expression, String values);

    public int addDB(String tableName, String[] columnName, String outPut, Object[] values);

    public int updateDB(String tableName, String[] columnName, Object[] values, String condition);

    public int deleteDB(String tableName, String condition);
}
