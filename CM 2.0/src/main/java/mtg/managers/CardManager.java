package mtg.managers;

import db.dbmanager.IPersistence;
import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;
import mtg.fxmlControllers.IController;
import mtg.magicthegatheringcm.Constants;
import mtg.magicthegatheringcm.MainApp;

public class CardManager
{

    private final File cardListFile = new File(Constants.CARD_COLLECTION_PATH);

    private static List<Card> allCards;
    private final IPersistence persistence;
    private final IController controller;

    public List<Card> getCards()
    {
        return allCards;
    }

    public CardManager(IPersistence persistence, IController controller)
    {
        this.persistence = persistence;
        this.controller = controller;

    }

    public void updateLocalDatabase()
    {

        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println("Calling API for All Cards");
                allCards = CardAPI.getAllCards();
                System.out.println("Done getting Cards");

                // TODO: update database
            }
        };
        MainApp.getThreadpool().execute(run);
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
                    allCards = CardAPI.getAllCards(filters);
                    Collections.reverse(allCards);
                    System.out.println("Done getting Cards");
                }
                catch (Exception e)
                {
                    System.err.println(e);
                }

            }
        };
        MainApp.getThreadpool().execute(run);
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
                    allCards = (List<Card>) ois.readObject();
                    Collections.reverse(allCards);
                    controller.refresh();
                    System.out.println("Done Reading cards from file");
                }
                catch (IOException | ClassNotFoundException e)
                {
                    System.err.println(e);
                }
            }
        };
        MainApp.getThreadpool().execute(run);
    }

    /**
     * <p>
     * Writing all cards from List to a file at the standard location</p>
     *
     * @throws java.lang.Exception
     */
    public void WriteAllCardsToFile() throws Exception
    {
        if (allCards.size() <= 0)
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
                    Collections.reverse(allCards);
                    oos.writeObject(allCards);
                    System.out.println("Done Writing cards to file");
                }
                catch (IOException e)
                {
                    System.err.println(e);
                }
            }
        };
        MainApp.getThreadpool().execute(run);
    }

    public List<Card> getPageOfCards(int startIndex)
    {
        
        
        return null;
    }
}
