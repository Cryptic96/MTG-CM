package mtg.database;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author theun
 */
public class DBStatements
{

    private Map<String, String> queries;

    public Map<String, String> getQueries()
    {
        return queries;
    }

    public DBStatements()
    {
        queries = new HashMap<>();
        fillQueries();

    }

    public void fillQueries()
    {
        queries.put("createCardTable",
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
}
