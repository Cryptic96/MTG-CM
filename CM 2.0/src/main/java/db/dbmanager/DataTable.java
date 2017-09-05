/*
 * Made by Laurent Lardenois, do not use without giving credit.
 * DISCLAIMER: I am not responsible if you dun goofed.
 */
package db.dbmanager;

import db.exceptions.TableRowAlreadyExists;
import db.exceptions.TableRowNotFound;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author laurent
 */
public class DataTable
{

    /**
     * The rows of the <code>DataTable</code>.
     */
    private final Map<String, Object> rows;

    /**
     * Initialises an empty <code>Datatable</code> for your data.
     */
    public DataTable()
    {
        rows = new HashMap<>();
    }

    /**
     * This method gives you the value of the requested row in the
     * <code>DataTable</code>.
     *
     * @param rowName The name of the row you want to read the data from, this
     * is usually the same name it has in the database.
     * @return The value of the given <code>rowName</code>.
     * @throws TableRowNotFound
     */
    public Object getValue(String rowName)
            throws TableRowNotFound
    {
        Object ret = rows.get(rowName);

        if (ret != null)
        {
            return ret;
        }
        throw new TableRowNotFound("No row found with name:" + rowName);
    }

    /**
     * Adds a row with data to the <code>DataTable</code>.
     *
     * @param identifier The name of the row.
     * @param value The value of the row.
     * @throws TableRowAlreadyExists
     */
    public void addRow(String identifier, Object value)
            throws TableRowAlreadyExists
    {
        if (!rows.containsKey(identifier))
        {
            rows.put(identifier, value);
        }
        throw new TableRowAlreadyExists("No row found with name:" + identifier);
    }
}
