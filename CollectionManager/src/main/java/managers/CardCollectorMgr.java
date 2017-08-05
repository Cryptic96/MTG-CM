package managers;

import api.CardAPI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import resource.Card;
import java.util.List;

public class CardCollectorMgr {

    private static final String CARD_COLLECTION_PATH = "./files/cardCollection.clist";
    private final File file = new File(CARD_COLLECTION_PATH);
    private List<Card> cards;

    public CardCollectorMgr() {
    }

    public List<Card> getCards() {
        return cards;
    }

    /**
     * Call all cards from API Write the list to File -> CARD_COLLECTION_PATH
     */
    public void callAllCards() {

        (new Thread(() -> {
            try {

                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                System.out.println("Gettring All Cards");
                cards = CardAPI.getAllCards();
                System.out.println("Done getting All Cards");

                oos.writeObject(cards);

            } catch (IOException e) {
                System.err.println(e);
            }
        })).start();
    }

    /**
     * Read all cards from File
     */
    public void readAllCards() {

        try {

            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            cards = (List<Card>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e);
        }
    }
}
