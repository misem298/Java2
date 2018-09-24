package chat.server;
import java.sql.*;
import java.util.Arrays;

public class AuthService {
    private String log;
    private String pass;
    private String nick;
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    boolean authorized = false;
    public AuthService(ClientHandler ch, String msg) {
        String[] date = msg.split("\\s");
        System.out.println(Arrays.toString(date));
        try {
            ConnectionMySql mysql = new ConnectionMySql();
           ResultSet rs = mysql.execute("SELECT * FROM logs ;");
            while (rs.next()) {
                System.out.println(rs.toString());
                log = rs.getString("login");
                pass = rs.getString("password");
                nick = rs.getString("nick");
                System.out.println(rs.toString() + log + pass);
                    if (log.equals(date[1]) && pass.equals(date[2])) {
                        authorized = true;
                        //mysql.closeMysql();
                        break;
                    }
                }
            } catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        public boolean getAuthorized () {
            return authorized;
        }
        public String getNick(){
        return nick;
        }
}