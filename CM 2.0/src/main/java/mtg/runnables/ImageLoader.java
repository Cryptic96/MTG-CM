package mtg.runnables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mtg.data.CardItem;
import mtg.magicthegatheringcm.Constants;
import mtg.magicthegatheringcm.MainApp;

/**
 *
 * @author Theun Schut
 */
public class ImageLoader implements Runnable
{

    
    private final List<CardItem> currentPage;

    public ImageLoader(List<CardItem> currentPage)
    {
        File imagePath = new File(Constants.IMAGE_FILE_PATH);
        imagePath.mkdir();
        this.currentPage = currentPage;
    }

    @Override
    public void run()
    {
        for (CardItem ci : currentPage)
        {
            try
            {
                ImageView imageView = ci.getCardImage();
                if (imageView.getImage() != null)
                {
                    continue;
                }

                File imageFile = new File(Constants.IMAGE_FILE_PATH + ci.getCard().getId());

                if (MainApp.getPropertySettings().get("cashImages") == "true")
                {
                    try (FileInputStream fis = new FileInputStream(imageFile))
                    {
                        // Try to find the image.
                        // If it's available, insert the image into the imageview
                        imageView.setImage(new Image(fis));
                    }
                    catch (FileNotFoundException FNFE)
                    {
                        // If the Image file is not available, 
                        // download the image instead.
                        final Image image = new Image(ci.getCard().getImageUrl());
                        imageView.setImage(image);
                        
                        // After download, create thread to write the file to disk
                        ImageWriter iw = new ImageWriter(ci.getCard());
                        MainApp.getThreadpool().execute(iw);
                    }
                }

                if (MainApp.getPropertySettings().get("cashImages") == "false")
                {
                    imageView.setImage(new Image(ci.getCard().getImageUrl()));
                }
            }
            catch (IOException ex)
            {
                System.err.println(ex);
            }
        }
    }
}
