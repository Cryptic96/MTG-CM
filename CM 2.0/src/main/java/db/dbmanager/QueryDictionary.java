/*
 * Made by Laurent Lardenois, do not use without giving credit.
 * DISCLAIMER: I am not responsible if you dun goofed.
 */
package db.dbmanager;

import db.exceptions.DictionaryCommandNotFound;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class contains all the queries that you will use on the database.
 *
 * @author laurent
 */
public class QueryDictionary
{

    /**
     * The collection of all your queries, and function/procedure calls.
     */
    private Map<String, String> dictionary = new ConcurrentHashMap<>();

    /**
     * <p>
     * The constructor of the QueryDictionary, here you can add all your
     * queries, and function/procedure calls by hand.
     * </p>
     *
     * <p>
     * CommandQuery should be replaced with the desired name to call the query.
     * Command should be replaced with a command, parameters are a "?" for each
     * parameter in the command.
     * </p>
     *
     * <p>
     * Command notations:
     * <ul>
     * <li><b>Query</b>: "SELECT * FROM * WHERE * = ? AND * = ?"</li>
     * <li><b>Function</b>: "{? = CALL FUNTION_NAME(?, ?)}"</li>
     * <li><b>Procedure</b>: "{CALL PROCEDURE_NAME(?, ?)}"</li>
     * </ul>
     * </p>
     */
    public QueryDictionary()
    {
        dictionary.put("createCardTable",
                "CREATE TABLE CARD ("
                + "ID                   TEXT    PRIMARY KEY     NOT NULL,"
                + "LAYOUT               TEXT, "
                + "NAME                 TEXT    NOT NULL, "
                + "MANA_COST            TEXT, "
                + "CMC                  NUMBER, "
                + "TYPE                 TEXT, "
                + "RARITY               TEXT, "
                + "TEXT                 TEXT, "
                + "ORIGINAL_TEXT        TEXT, "
                + "FLAVOUR              TEXT, "
                + "ARTIST               TEXT, "
                + "NUMBER               TEXT, "
                + "POWER                TEXT, "
                + "TOUGHNESS            TEXT, "
                + "LOYALTY              INT, "
                + "MULTIVERSE_ID        INT, "
                + "IMAGE_NAME           TEXT, "
                + "WATERMARK            TEXT, "
                + "BORDER               TEXT, "
                + "TIME_SHIFTED         INT, "
                + "HAND                 INT, "
                + "LIFE                 INT, "
                + "RESERVED             INT, "
                + "RELEASE_DATE         TEXT, "
                + "STARTER              INT, "
                + "SET_                 TEXT, "
                + "SET_NAME             TEXT, "
                + "IMAGE_URL            TEXT);");
    }

    /**
     * This method returns the value of the dictionary with the specified
     * CommandName.
     *
     * @param commandName The name of the command you want to use.
     * @return The command you are using.
     * @throws DictionaryCommandNotFound
     */
    public String getCommand(String commandName)
            throws DictionaryCommandNotFound
    {
        String ret = dictionary.get(commandName);

        if (ret != null)
        {
            return ret;
        }
        throw new DictionaryCommandNotFound(
                "No command found with name:" + commandName);
    }
}
