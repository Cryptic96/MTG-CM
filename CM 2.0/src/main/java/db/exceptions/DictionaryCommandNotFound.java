/*
 * Made by Laurent Lardenois, do not use without giving credit.
 * DISCLAIMER: I am not responsible if you dun goofed.
 */
package db.exceptions;

/**
 *
 * @author laurent
 */
public class DictionaryCommandNotFound extends Exception
{

    /**
     * Creates a new instance of <code>DictionaryCommandNotFoundException</code>
     * without detail message.
     */
    public DictionaryCommandNotFound()
    {
        
    }

    /**
     * Constructs an instance of <code>DictionaryCommandNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public DictionaryCommandNotFound(String msg)
    {
        super(msg);
    }
}
