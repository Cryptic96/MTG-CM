/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtg.fxmlControllers;

import com.sun.javafx.property.adapter.PropertyDescriptor;
import io.magicthegathering.javasdk.resource.Card;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import mtg.managers.CardManager;

/**
 * FXML Controller class
 *
 * @author Theun Schut
 */
public class CardOverviewController implements Initializable, IController
{

    private int cardStartIndex = 0;
    private CardManager cardManager;
    private List<TitledPane> cardPanes;
    private TitledPane selectedTitledPane;
    private Image defaultImage;
    private static final String DEFAULT_STRING = "No Data yet";
    // Buttons 
    @FXML
    private MenuItem btnLoadFromAPI;
    @FXML
    private MenuItem btnLoadFromFile;
    @FXML
    private MenuItem btnWriteToFile;
    @FXML
    private ProgressIndicator progressIndicator;

    // Card View
    @FXML
    private Accordion accordionCards;
    @FXML
    private ImageView cardImage;

    // Page Controls
    @FXML
    private Label lblPage;

    public CardOverviewController()
    {
        try
        {
            this.defaultImage = new Image(new FileInputStream("./files/default_image.png"));
        } catch (FileNotFoundException ex)
        {
            System.err.println(ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.cardManager = new CardManager(this);
        this.cardPanes = new ArrayList<>();

        // fill cards at the beginning
        cardManager.ReadAllCardsFromFile();
    }

    @FXML
    private void ButtonLoadFromAPI(ActionEvent event)
    {
        btnLoadFromAPI.setDisable(true);
        btnLoadFromFile.setDisable(true);
        btnWriteToFile.setDisable(true);
        //progressIndicator.setVisible(true);

        cardManager.CallAllCardsFromAPI();
    }

    @FXML
    private void ButtonLoadFromFile(ActionEvent event)
    {
        btnLoadFromAPI.setDisable(true);
        btnLoadFromFile.setDisable(true);
        btnWriteToFile.setDisable(true);
        //progressIndicator.setVisible(true);

        cardManager.ReadAllCardsFromFile();
    }

    @FXML
    private void ButtonWriteToFile(ActionEvent event)
    {
        btnLoadFromAPI.setDisable(true);
        btnLoadFromFile.setDisable(true);
        btnWriteToFile.setDisable(true);
        //progressIndicator.setVisible(true);

        cardManager.WriteAllCardsToFile();
    }

    @FXML
    private void ButtonPreviousPage(ActionEvent event)
    {
        if (cardStartIndex == 0)
        {
            return;
        }

        cardStartIndex = cardStartIndex - 100;
        refreshCardPages(cardStartIndex);
    }

    @FXML
    private void ButtonNextPage(ActionEvent event)
    {
        if ((cardStartIndex % 100) != 0)
        {
            return;
        }

        cardStartIndex = cardStartIndex + 100;
        refreshCardPages(cardStartIndex);
    }

    @Override
    public void refresh()
    {
        try
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    cardStartIndex = 0;
                    CardToTitlePaneConverter();
                    refreshCardPages(cardStartIndex);

                    btnLoadFromAPI.setDisable(false);
                    btnLoadFromFile.setDisable(false);
                    btnWriteToFile.setDisable(false);
//                    progressIndicator.setVisible(false);
                }
            });
        } catch (Exception e)
        {
            System.err.println(e);
        }
    }

    private void CardToTitlePaneConverter()
    {
        for (Card c : cardManager.getCards())
        {
            final TitledPane tp = new TitledPane();
            tp.setUserData(c);
            if (c.getReleaseDate() == null)
            {
                tp.setText(c.getName() + " - (unknown date)");
            } else
            {
                tp.setText(c.getName() + " - (" + c.getReleaseDate() + ")");
            }
            tp.setContent(addCardComponents());

            tp.heightProperty().addListener(new ChangeListener<Number>()
            {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
                {
                    Card c = (Card) tp.getUserData();
                    Pane pane = (Pane) tp.getContent();
                    Pane imagePane = (Pane) pane.getChildren().get(0);
                    ImageView imageView = (ImageView) imagePane.getChildren().get(0);

                    if (imageView.getImage() == null && tp.getHeight() > 50)
                    {
                        if (c.getImageUrl() == null)
                        {
                            fillFieldData(tp);
                            if (defaultImage == null)
                            {
                                InputStream in;
                                try
                                {
                                    in = new FileInputStream("./files/default_image.png");
                                    defaultImage = new Image(in);
                                    imageView.setImage(defaultImage);
                                } catch (FileNotFoundException ex)
                                {
                                    System.err.println(ex.getMessage());
                                }
                            } else
                            {
                                imageView.setImage(defaultImage);
                            }
                        } else
                        {
                            imageView.setImage(new Image(c.getImageUrl()));
                        }
                    }
                }
            });

            cardPanes.add(tp);
        }
    }

    private Pane addCardComponents()
    {
        final Pane pane = new Pane();
        pane.setPrefHeight(500);

        // image pane
        Pane imagePane = new Pane();
        imagePane.setPrefSize(226, 311);
        imagePane.setLayoutX(15);
        imagePane.setLayoutY(15);
        imagePane.setStyle("-fx-border-color: #000");
        pane.getChildren().add(imagePane);

        // imageview
        ImageView imageview = new ImageView();
        imageview.setFitWidth(226);
        imageview.setFitHeight(311);
        imageview.setLayoutX(0);
        imageview.setLayoutY(0);
        imagePane.getChildren().add(imageview);

        // panel for labels for the card fields
        final ScrollPane labelScrollPane = new ScrollPane();
        labelScrollPane.setPrefSize(600, 311);
        labelScrollPane.setLayoutX(256);
        labelScrollPane.setLayoutY(15);
        labelScrollPane.setStyle("-fx-border-color: #000");
        pane.getChildren().add(labelScrollPane);
        pane.widthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                labelScrollPane.setPrefWidth(pane.getWidth() - 270);
                pane.getWidth();
            }
        });

        //put Pane in scrollpane
        Pane labelPane = new Pane();
        //labelPane.setPrefSize(600, 311);
//        labelPane.setLayoutX(256);
//        labelPane.setLayoutY(15);
        labelScrollPane.setContent(labelPane);

        // add Strings to label
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(DEFAULT_STRING);

        Label lblCollection = new Label(sBuilder.toString());
        labelPane.getChildren().add(lblCollection);
        return pane;
    }

    private void refreshCardPages(int begin)
    {
        accordionCards.getPanes().clear();
        for (int i = begin; i < (begin + 100); i++)
        {
            TitledPane tp = cardPanes.get(i);
            accordionCards.getPanes().add(tp);
        }
        lblPage.setText((cardStartIndex + 1) + " - " + (cardStartIndex + 100));

        // Thread image loading shizzle
        new Thread(new FillImages(begin)).start();
    }

    void fillFieldData(TitledPane tp)
    {
        Pane pane = (Pane) tp.getContent();
        Pane labelPane = (Pane) ((ScrollPane) pane.getChildren().get(1)).getContent();
        final Label label = (Label) labelPane.getChildren().get(0);
        if (label.getText().length() > 15)
        {
            return;
        }
        Card c = (Card) tp.getUserData();
        // add Strings to label
        final StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("ID: ");
        sBuilder.append(c.getId());
        sBuilder.append("\n");
        sBuilder.append("Layout: ");
        sBuilder.append(c.getLayout());
        sBuilder.append("\n");
        sBuilder.append("Name: ");
        sBuilder.append(c.getName());
        sBuilder.append("\n");
        sBuilder.append("Names: ");
        sBuilder.append(c.getNames());
        sBuilder.append("\n");
        sBuilder.append("Mana cost: ");
        sBuilder.append(c.getManaCost());
        sBuilder.append("\n");
        sBuilder.append("CMC: ");
        sBuilder.append(c.getCmc());
        sBuilder.append("\n");
        sBuilder.append("Colours: ");
        sBuilder.append(c.getColors());
        sBuilder.append("\n");
        sBuilder.append("Colour Identity: ");
        sBuilder.append(c.getColorIdentity());
        sBuilder.append("\n");
        sBuilder.append("Type: ");
        sBuilder.append(c.getType());
        sBuilder.append("\n");
        sBuilder.append("Supertypes: ");
        sBuilder.append(c.getSupertypes());
        sBuilder.append("\n");
        sBuilder.append("Types: ");
        sBuilder.append(c.getTypes());
        sBuilder.append("\n");
        sBuilder.append("SubTypes: ");
        sBuilder.append(c.getSubtypes());
        sBuilder.append("\n");
        sBuilder.append("Rarity: ");
        sBuilder.append(c.getRarity());
        sBuilder.append("\n");
        sBuilder.append("Text: ");
        sBuilder.append(c.getText());
        sBuilder.append("\n");
        sBuilder.append("Original Text: ");
        sBuilder.append(c.getOriginalText());
        sBuilder.append("\n");
        sBuilder.append("Flavour: ");
        sBuilder.append(c.getFlavor());
        sBuilder.append("\n");
        sBuilder.append("Artist: ");
        sBuilder.append(c.getArtist());
        sBuilder.append("\n");
        sBuilder.append("Number: ");
        sBuilder.append(c.getNumber());
        sBuilder.append("\n");
        sBuilder.append("Power: ");
        sBuilder.append(c.getPower());
        sBuilder.append("\n");
        sBuilder.append("Toughness: ");
        sBuilder.append(c.getToughness());
        sBuilder.append("\n");
        sBuilder.append("Loyalty: ");
        sBuilder.append(c.getLoyalty());
        sBuilder.append("\n");
        sBuilder.append("Multiverse ID: ");
        sBuilder.append(c.getMultiverseid());
        sBuilder.append("\n");
        sBuilder.append("Variations: ");
        sBuilder.append(c.getVariations());
        sBuilder.append("\n");
        sBuilder.append("Image Name: ");
        sBuilder.append(c.getImageName());
        sBuilder.append("\n");
        sBuilder.append("Watermark: ");
        sBuilder.append(c.getWatermark());
        sBuilder.append("\n");
        sBuilder.append("Border: ");
        sBuilder.append(c.getBorder());
        sBuilder.append("\n");
        sBuilder.append("Time Shifted: ");
        sBuilder.append(c.isTimeshifted());
        sBuilder.append("\n");
        sBuilder.append("Hand: ");
        sBuilder.append(c.getHand());
        sBuilder.append("\n");
        sBuilder.append("Life: ");
        sBuilder.append(c.getLife());
        sBuilder.append("\n");
        sBuilder.append("Reserved: ");
        sBuilder.append(c.isReserved());
        sBuilder.append("\n");
        sBuilder.append("Release Date: ");
        sBuilder.append(c.getReleaseDate());
        sBuilder.append("\n");
        sBuilder.append("Starter: ");
        sBuilder.append(c.isStarter());
        sBuilder.append("\n");
        sBuilder.append("Set: ");
        sBuilder.append(c.getSet());
        sBuilder.append("\n");
        sBuilder.append("Setname: ");
        sBuilder.append(c.getSetName());
        sBuilder.append("\n");
        sBuilder.append("Printings: ");
        sBuilder.append(c.getPrintings());
        sBuilder.append("\n");
        sBuilder.append("Image URL: ");
        sBuilder.append(c.getImageUrl());
        sBuilder.append("\n");
        final String answerS = sBuilder.toString();
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                label.setText(answerS);
            }
        });

    }

    private void clearFieldData(TitledPane tp)
    {
        Pane pane = (Pane) tp.getContent();
        Pane labelPane = (Pane) ((ScrollPane) pane.getChildren().get(1)).getContent();
        Label label = (Label) labelPane.getChildren().get(0);
        label.setText(DEFAULT_STRING);

    }

    class FillImages implements Runnable
    {

        int begin;

        public FillImages(int begin)
        {
            this.begin = begin;
        }

        @Override
        public void run()
        {
            for (int i = begin; i <= (begin + 100); i++)
            {
                TitledPane tp = cardPanes.get(i);
                // fill next image
                Pane pane = (Pane) tp.getContent();
                Pane imagePane = (Pane) pane.getChildren().get(0);
                ImageView imageView = (ImageView) imagePane.getChildren().get(0);
                fillFieldData(tp);
                Card c = (Card) tp.getUserData();
                if (imageView.getImage() == null && c.getImageUrl() != null)
                {
                    imageView.setImage(new Image(c.getImageUrl()));
                }

                if (cardStartIndex != begin)
                {
                    break;
                }
            }

            // Wait for User to change page
            while (cardStartIndex == begin)
            {
                try
                {
                    Thread.sleep(100);
                } catch (InterruptedException e)
                {
                }
            }
            // destruct images in thread
            for (int i = begin; i <= (begin + 100); i++)
            {
                // empty all images
                TitledPane tp = cardPanes.get(i);
                Pane pane = (Pane) tp.getContent();
                Pane imagePane = (Pane) pane.getChildren().get(0);
                ImageView imageView = (ImageView) imagePane.getChildren().get(0);
                clearFieldData(tp);
                if (imageView.getImage() != null)
                {
                    imageView.setImage(null);
                }

                // Stop cleaning if User goes back to this page
                if (cardStartIndex == begin)
                {
                    return;
                }
            }
        }
    }
}
