package mtg.managers;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import mtg.magicthegatheringcm.MainApp;

public class CardManager
{

    private static final String CARD_COLLECTION_PATH = "./files/all_cards.clist";
    private final File cardListFile = new File(CARD_COLLECTION_PATH);

    private static List<Card> cards;

    public List<Card> getCards()
    {
        return cards;
    }

    public CardManager()
    {
        
    }
    
    public void updateLocalDatabase()
    {
        
        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    System.out.println("Calling API for All Cards");
                    cards = CardAPI.getAllCards();
                    System.out.println("Done getting Cards");
                    
                    DatabaseManager db = new DatabaseManager();
                    db.updateDatabase(cards);
                }
                catch (ClassNotFoundException | SQLException e)
                {
                    System.err.println(e);
                }
            }
        };
        MainApp.getThreadpoolExecutorService().execute(run);
    }

    /**
     *
     * @param filters
     */
    public void CallCardsFromAPI(final List<String> filters)
    {
        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    System.out.println("Calling API for Filtered Cards");
                    cards = CardAPI.getAllCards(filters);
                    Collections.reverse(cards);
                    System.out.println("Done getting Cards");
                }
                catch (Exception e)
                {
                    System.err.println(e);
                }

            }
        };
        MainApp.getThreadpoolExecutorService().execute(run);
    }

    /**
     * <p>
     * Loading file from standard location with all the known cards</p>
     */
    public void ReadAllCardsFromFile()
    {
        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    System.out.println("Starting to read cards from file");
                    FileInputStream fis = new FileInputStream(cardListFile);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    cards = (List<Card>) ois.readObject();
                    Collections.reverse(cards);
                    System.out.println("Done Reading cards from file");
                }
                catch (IOException | ClassNotFoundException e)
                {
                    System.err.println(e);
                }
            }
        };
        MainApp.getThreadpoolExecutorService().execute(run);
    }

    /**
     * <p>
     * Writing all cards from List to a file at the standard location</p>
     *
     * @throws java.lang.Exception
     */
    public void WriteAllCardsToFile() throws Exception
    {
        if (cards.size() <= 0)
        {
            throw new Exception("There are no cards in local list!");
        }

        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    System.out.println("Starting to write cards to file");
                    FileOutputStream fos = new FileOutputStream(cardListFile);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    Collections.reverse(cards);
                    oos.writeObject(cards);
                    System.out.println("Done Writing cards to file");
                }
                catch (IOException e)
                {
                    System.err.println(e);
                }
            }
        };
        MainApp.getThreadpoolExecutorService().execute(run);
    }
}
