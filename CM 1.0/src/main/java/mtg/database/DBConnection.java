package mtg.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author theun
 */
public class DBConnection {

    private Connection c = null;
    private DBStatements dBStatements = null;
    
    private Connection getConnection(){
        return c;
    }
    
    public DBConnection() {
    }
    
    private Connection createNewConnection() throws ClassNotFoundException, SQLException {
        c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:mtgCards.db");
            System.out.println("Opened database successfully");
            return c;
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        }
    }
}
