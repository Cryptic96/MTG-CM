/*
 * Made by Laurent Lardenois, do not use without giving credit.
 * DISCLAIMER: I am not responsible if you dun goofed.
 */
package db.dbmanager;

import db.exceptions.DictionaryCommandNotFound;
import db.exceptions.TableRowAlreadyExists;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * This interface has all the database communication methods. To use it,
 * instantiate a class that uses this interface.
 *
 * @author laurent
 */
public interface IPersistence
{

    public void init(List<Class<? extends Serializable>> classes)
            throws SQLException, FileNotFoundException, IOException,
            ClassNotFoundException;

    public DataTable executeReturnCommand(
            String commandName, LinkedList<Object> parameters)
            throws SQLException, DictionaryCommandNotFound,
            TableRowAlreadyExists, IOException;

    public void executeNonReturnCommand(
            String commandName, LinkedList<Object> parameters)
            throws SQLException, DictionaryCommandNotFound;
}
