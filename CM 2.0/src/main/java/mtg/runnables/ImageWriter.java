package mtg.runnables;

import io.magicthegathering.javasdk.resource.Card;
import java.io.File;
import mtg.magicthegatheringcm.Constants;

/**
 *
 * @author Theun Schut
 */
public class ImageWriter implements Runnable
{

    private Card card;
    
    public ImageWriter(Card card)
    {
        this.card = card;
    }

    
    @Override
    public void run()
    {
        File imageFile = new File(Constants.IMAGE_FILE_PATH + card.getId());
        
        // TODO: write image to disk
    }
    
}
