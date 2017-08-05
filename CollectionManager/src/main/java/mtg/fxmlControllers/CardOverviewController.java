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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import mtg.managers.CardManager;

/**
 * FXML Controller class
 *
 * @author Theun Schut
 */
public class CardOverviewController implements Initializable, IController {

    private int beginAmount = 0;
    private CardManager cardManager;
    private List<TitledPane> cardPanes;

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
        if (beginAmount == 0) {
            return;
        }

        beginAmount = beginAmount - 100;
        refreshCards(beginAmount);
    }

    @FXML
    private void ButtonNextPage(ActionEvent event) {
        if ((beginAmount % 100) != 0) {
            return;
        }

        beginAmount = beginAmount + 100;
        refreshCards(beginAmount);
    }

    @Override
    public void refresh() {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    beginAmount = 0;

                    for (Card c : cardManager.getCards()) {
                        TitledPane tp = new TitledPane();
                        tp.setUserData(c);
                        tp.setText(c.getName());
                        cardPanes.add(tp);
                    }

                    Image image = new Image(cardManager.getCards().get(1).getImageUrl());
                    cardImage.setImage(image);

                    refreshCards(beginAmount);

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

    private void refreshCards(int begin) {
        accordionCards.getPanes().clear();
        for (int i = begin; i <= (begin + 100); i++) {
            TitledPane tp = cardPanes.get(i);
            accordionCards.getPanes().add(tp);
        }
        lblPage.setText(beginAmount + " - " + (beginAmount + 100));
    }
}
