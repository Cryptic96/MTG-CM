/*
 * Made by Laurent Lardenois, do not use without giving credit.
 * DISCLAIMER: I am not responsible if you dun goofed.
 */
package db.exceptions;

/**
 *
 * @author laurent
 */
public class TableRowAlreadyExists extends Exception
{

    /**
     * Creates a new instance of <code>TableRowAlreadyExistsException</code>
     * without detail message.
     */
    public TableRowAlreadyExists()
    {
    }

    /**
     * Constructs an instance of <code>TableRowAlreadyExistsException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public TableRowAlreadyExists(String msg)
    {
        super(msg);
    }
}
