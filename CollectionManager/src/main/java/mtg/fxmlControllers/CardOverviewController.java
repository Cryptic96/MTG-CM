/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtg.fxmlControllers;

import io.magicthegathering.javasdk.resource.Card;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import mtg.managers.CardManager;

/**
 * FXML Controller class
 *
 * @author Theun Schut
 */
public class CardOverviewController implements Initializable, IController {

    private int cardStartIndex = 0;
    private CardManager cardManager;
    private List<TitledPane> cardPanes;
    private TitledPane selectedTitledPane;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.cardManager = new CardManager(this);
        this.cardPanes = new ArrayList<>();
    }

    @FXML
    private void ButtonLoadFromAPI(ActionEvent event) {
        btnLoadFromAPI.setDisable(true);
        btnLoadFromFile.setDisable(true);
        btnWriteToFile.setDisable(true);
        //progressIndicator.setVisible(true);

        cardManager.CallAllCardsFromAPI();
    }

    @FXML
    private void ButtonLoadFromFile(ActionEvent event) {
        btnLoadFromAPI.setDisable(true);
        btnLoadFromFile.setDisable(true);
        btnWriteToFile.setDisable(true);
        //progressIndicator.setVisible(true);

        cardManager.ReadAllCardsFromFile();
    }

    @FXML
    private void ButtonWriteToFile(ActionEvent event) {
        btnLoadFromAPI.setDisable(true);
        btnLoadFromFile.setDisable(true);
        btnWriteToFile.setDisable(true);
        //progressIndicator.setVisible(true);

        cardManager.WriteAllCardsToFile();
    }

    @FXML
    private void ButtonPreviousPage(ActionEvent event) {
        if (cardStartIndex == 0) {
            return;
        }

        cardStartIndex = cardStartIndex - 100;
        refreshCardPages(cardStartIndex);
    }

    @FXML
    private void ButtonNextPage(ActionEvent event) {
        if ((cardStartIndex % 100) != 0) {
            return;
        }

        cardStartIndex = cardStartIndex + 100;
        refreshCardPages(cardStartIndex);
    }

    @Override
    public void refresh() {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    cardStartIndex = 0;
                    CardToTitlePaneConverter();
                    refreshCardPages(cardStartIndex);

                    btnLoadFromAPI.setDisable(false);
                    btnLoadFromFile.setDisable(false);
                    btnWriteToFile.setDisable(false);
//                    progressIndicator.setVisible(false);
                }
            });
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void CardToTitlePaneConverter() {
        for (Card c : cardManager.getCards()) {
            final TitledPane tp = new TitledPane();
            tp.setUserData(c);
            tp.setText(c.getName());
            tp.setContent(addCardComponents());

            tp.heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    Card c = (Card) tp.getUserData();
                    Pane pane = (Pane) tp.getContent();
                    Pane imagePane = (Pane) pane.getChildren().get(0);
                    ImageView imageView = (ImageView) imagePane.getChildren().get(0);

                    if (tp.getHeight() < 50) {
                        imageView.setImage(null);
                    } else {
                        imageView.setImage(new Image(c.getImageUrl()));
                    }
                }
            });

//            tp.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
//                @Override
//                public void handle(javafx.scene.input.MouseEvent event) {
//                    Card c = (Card) tp.getUserData();
//                    Pane pane = (Pane) tp.getContent();
//                    Pane imagePane = (Pane) pane.getChildren().get(0);
//                    ImageView imageView = (ImageView) imagePane.getChildren().get(0);
//                    imageView.setImage(new Image(c.getImageUrl()));
//                }
//            });
            cardPanes.add(tp);
        }
    }

    private Pane addCardComponents() {
        Pane pane = new Pane();
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

        return pane;
    }

    private void refreshCardPages(int begin) {
        accordionCards.getPanes().clear();
        for (int i = begin; i < (begin + 100); i++) {
            TitledPane tp = cardPanes.get(i);
            accordionCards.getPanes().add(tp);
        }
        lblPage.setText((cardStartIndex + 1) + " - " + (cardStartIndex + 100));

        // Thread image loading shizzle
        new Thread(new FillImages(begin)).start();
    }

    class FillImages implements Runnable {

        int begin;

        public FillImages(int begin) {
            this.begin = begin;
        }

        @Override
        public void run() {
            int lastIndex;
            for (int i = begin; i <= (begin + 100); i++) {
                // fill next image
                
                if (cardStartIndex != begin) {
                    lastIndex = i;
                    break;
                }
            }
            while(cardStartIndex == begin){
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
            // destruct images in thread
            for (int i = begin; i <= (begin + 100); i++) {
                // fill next image
                
                if (cardStartIndex != begin) {
                    lastIndex = i;
                    break;
                }
            }
        }

    }
}
