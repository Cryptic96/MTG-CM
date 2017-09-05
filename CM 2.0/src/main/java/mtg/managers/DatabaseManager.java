package mtg.managers;

import io.magicthegathering.javasdk.resource.Card;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import mtg.database.DBStatements;

/**
 *
 * @author theun
 */
public class DatabaseManager
{

    private static Connection connection = null;
    private DBStatements statements = null;
    private Statement stmt;

    public Connection getConnection()
            throws ClassNotFoundException, SQLException
    {
        if (connection != null)
        {
            return connection;
        }

        return createNewConnection();
    }

    public void closeConnection() throws SQLException
    {
        connection.close();
    }

    public DatabaseManager() throws ClassNotFoundException, SQLException
    {
        this.statements = new DBStatements();
        getConnection();
    }

    private Connection createNewConnection()
            throws ClassNotFoundException, SQLException
    {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:mtgCards.db");
        stmt = connection.createStatement();

        try
        {
            stmt.execute("SELECT * FROM CARDS;");
        }
        catch (SQLException e)
        {
            try
            {
                System.out.println("Creating Tables");
                stmt.execute(statements.getQueries().get("createCardTable"));
            }
            catch (SQLException ex)
            {
                System.err.println(ex);
            }

        }

        return connection;
    }

    public void updateDatabase(List<Card> cards) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        for (Card c : cards)
        {

        }
    }
}
