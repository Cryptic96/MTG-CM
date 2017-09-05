/*
 * Made by Laurent Lardenois, do not use without giving credit.
 * DISCLAIMER: I am not responsible if you dun goofed.
 */
package db.exceptions;

/**
 *
 * @author laurent
 */
public class TableRowNotFound extends Exception
{

    /**
     * Creates a new instance of <code>TableRowNotFoundException</code> without
     * detail message.
     */
    public TableRowNotFound()
    {
    }

    /**
     * Constructs an instance of <code>TableRowNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TableRowNotFound(String msg)
    {
        super(msg);
    }
}
