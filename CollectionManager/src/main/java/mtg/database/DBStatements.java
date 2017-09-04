package mtg.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author theun
 */
public class DBStatements {

    private Connection c = null;
    private Statement stmt = null;

    public DBStatements(Connection c) {
        if (c != null) {
            this.c = c;
        } else {
            throw new NullPointerException("Connection is null");
        }
    }

    public void createTables() throws SQLException {
        try {
            stmt = c.createStatement();
            String sql
                    = "CREATE TABLE CARD ("
                    + " ID INT PRIMARY KEY     NOT NULL,"
                    + " NAME           TEXT    NOT NULL, "
                    + " AGE            INT     NOT NULL, "
                    + " ADDRESS        CHAR(50), "
                    + " SALARY         REAL)";
            stmt.execute(sql);
            stmt.close();
        } catch (SQLException e) {
            throw e;
        }
    }
}
