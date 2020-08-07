package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseDAOImplement implements BaseDAO {

    public static BaseDAOImplement istance = null;

    public static BaseDAOImplement getIstance() {
        if (istance == null) {
            istance = new BaseDAOImplement();
        }
        return istance;
    }

    @Override
    public Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String user = System.getenv("dbUserName");
            String pass = System.getenv("dbPass");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=QLSV";
            return DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BaseDAOImplement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BaseDAOImplement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getTableName(String suggestions) {
        String sql = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME LIKE '%" + suggestions + "%'";
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            return rs.getString("TABLE_NAME");
        } catch (SQLException ex) {
            Logger.getLogger(BaseDAOImplement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    @Override
    public String[] getColumnName(String tableName) {
        String sql = "SELECT * FROM " + tableName + "";
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int sumColumn = rsmd.getColumnCount();
            String[] columnName = new String[sumColumn];
            for (int i = 0; i < sumColumn; i++) {
                columnName[i] = rsmd.getColumnName(i + 1);
            }
            return columnName;
        } catch (SQLException ex) {
            Logger.getLogger(BaseDAOImplement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getCondition(String field, String expression, String values) {
        String condition = "WHERE " + field + " " + expression + " '" + values + "'";
        return condition;
    }

    @Override
    public int addDB(String tableName, String[] columnName, String outPut, Object[] values) {
        String sqlColumn = "";
        String sqlValues = "";
        String sql = "";
        for (int i = 0; i < columnName.length; i++) {
            if (i == columnName.length - 1) {
                sqlColumn += columnName[i];
                sqlValues += "?";
            } else {
                sqlColumn += columnName[i] + ",";
                sqlValues += "?,";
            }
        }
        if (outPut.trim().equals("")) {
            sql = "INSERT INTO " + tableName + " (" + sqlColumn + ") VALUES(" + sqlValues + ")";
            try {
                PreparedStatement ps = getConnection().prepareCall(sql);
                for (int i = 0; i < values.length; i++) {
                    ps.setObject(i + 1, values[i]);
                }
                return ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(BaseDAOImplement.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            sql = "INSERT INTO " + tableName + " (" + sqlColumn + ") " + outPut + " VALUES(" + values + ")";
            try {
                PreparedStatement ps = getConnection().prepareCall(sql);
                for (int i = 0; i < values.length; i++) {
                    ps.setObject(i + 1, values[i]);
                }
                int addOB = ps.executeUpdate();
                if (addOB > 0) {
                    ResultSet rs = ps.getResultSet();
                    rs.next();
                    return rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(BaseDAOImplement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    @Override
    public int updateDB(String tableName, String[] columnName, Object[] values, String condition) {
        String sqlmin = "";
        for (int i = 0; i < columnName.length; i++) {
            if (i == columnName.length - 1) {
                sqlmin += columnName[i] + "= ?";
            } else {
                sqlmin += columnName[i] + "= ?,";
            }
        }
        String sql = "UPDATE " + tableName + " SET " + sqlmin + " " + condition + "";
        try {
            PreparedStatement ps = getConnection().prepareCall(sql);
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(BaseDAOImplement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public int deleteDB(String tableName, String condition) {
        String sql = "DELETE " + tableName + " " + condition + "";
        try {
            Statement statement = getConnection().createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(BaseDAOImplement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

}
