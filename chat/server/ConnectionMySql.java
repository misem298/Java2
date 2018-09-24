package chat.server;

import java.sql.*;

public class ConnectionMySql {
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public ConnectionMySql() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/logs?" +
                    "user=root&password=");
            stmt = conn.createStatement();
        } catch(SQLException ex){
            ex.printStackTrace();
       }
    }
    public ResultSet execute(String cmd) {
        try { rs = stmt.executeQuery(cmd);
        } catch(SQLException e){
            rs = null;
            e.printStackTrace(); }
        return rs;
    }
    public int update(String cmd) {
        int upd = 0;
        try { upd = stmt.executeUpdate(cmd);
        } catch(SQLException e){
            e.printStackTrace(); }
        return upd;
    }
    public void closeMysql(){
        if (rs != null) {
                try {
                rs.close(); // close connection
                } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close(); // close statement
                } catch (SQLException sqlEx) {
                    sqlEx.printStackTrace();
                }
                stmt = null;
            }
    }
}
