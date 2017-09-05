package db.dbmanager;

import db.exceptions.DictionaryCommandNotFound;
import db.exceptions.TableRowAlreadyExists;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Theun Schut
 */
public class SQLITEPersistence implements IPersistence
{

    protected static QueryDictionary queries;
    protected static List<Class<? extends Serializable>> classes;
    protected static Connection connection;

    @Override
    public void init(List<Class<? extends Serializable>> classes) throws SQLException, FileNotFoundException, IOException, ClassNotFoundException
    {
        queries = new QueryDictionary();
        SQLITEPersistence.classes = classes;

        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:mtgCards.db");
        System.out.println("Connected to database");
        checkForTables();
    }

    @Override
    public DataTable executeReturnCommand(String commandName, LinkedList<Object> parameters) throws SQLException, DictionaryCommandNotFound, TableRowAlreadyExists, IOException
    {
        PreparedStatement statement = connection.prepareStatement(
                queries.getCommand(commandName));

        if (parameters.size() > 0)
        {
            setParameters(statement, parameters);
        }
        ResultSet rs = statement.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        DataTable returnTable = new DataTable();

        while (rs.next())
        {
            int rowID = rs.getRow();
            returnTable.addRow(
                    rsmd.getColumnName(rowID), rs.getObject(rowID));
        }
        return returnTable;
    }

    @Override
    public void executeNonReturnCommand(String commandName, LinkedList<Object> parameters) throws SQLException, DictionaryCommandNotFound
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private PreparedStatement setParameters(
            PreparedStatement statement, List<Object> parameters)
            throws SQLException, IOException
    {
        int i = 0;

        for (Object o : parameters)
        {
            if (classes.contains(o.getClass()))
            {
                o = serialiseObject(o);
            }
            statement.setObject(i, o);
            i++;
        }
        return statement;
    }

    private byte[] serialiseObject(Object object)
            throws IOException
    {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream())
        {
            try (ObjectOutputStream o = new ObjectOutputStream(b))
            {
                System.out.println(object.getClass().toString());
                o.writeObject(object);
            }
            return b.toByteArray();
        }

    }

    private Object deserialiseObject(byte[] bytes)
            throws IOException, ClassNotFoundException
    {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes))
        {
            try (ObjectInputStream o = new ObjectInputStream(b))
            {
                Object ret = o.readObject();
                System.out.println(ret.getClass().toString());
                return ret;
            }
        }
    }

    private void checkForTables()
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM CARD;");
            ResultSet rs = statement.executeQuery();
            System.out.println("Found data in database");
        }
        catch (SQLException e)
        {
            System.err.println(e);
            try
            {
                System.out.println("No tables available");
                System.out.println("Creating tables");
                PreparedStatement statement = connection.prepareStatement(queries.getCommand("createCardTable"));
                statement.execute();
            }
            catch (SQLException | DictionaryCommandNotFound ex)
            {
                System.err.println(ex);
            }
        }
    }
}
