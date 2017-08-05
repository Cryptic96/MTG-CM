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
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TitledPane;
import mtg.managers.CardManager;

/**
 * FXML Controller class
 *
 * @author Theun Schut
 */
public class CardOverviewController implements Initializable, IController {

    private CardManager cardManager;

    private List<TitledPane> cardPanes;
    
    @FXML
    private Accordion accordionPaneCards;
    @FXML
    private Button btnLoadFromAPI;
    @FXML
    private Button btnLoadFromFile;
    @FXML
    private Button btnWriteToFile;
    @FXML
    private ProgressIndicator progressIndicator;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
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
        progressIndicator.setVisible(true);
        
        cardManager.CallAllCardsFromAPI();
    }
    
    @FXML
    private void ButtonLoadFromFile(ActionEvent event) {
        btnLoadFromAPI.setDisable(true);
        btnLoadFromFile.setDisable(true);
        btnWriteToFile.setDisable(true);
        progressIndicator.setVisible(true);
        
        cardManager.ReadAllCardsFromFile();
    }
    
    @FXML
    private void ButtonWriteToFile(ActionEvent event) {
        btnLoadFromAPI.setDisable(true);
        btnLoadFromFile.setDisable(true);
        btnWriteToFile.setDisable(true);
        progressIndicator.setVisible(true);
        
        cardManager.WriteAllCardsToFile();
    }

    @Override
    public void refresh() {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    cardPanes.clear();
                    for (Card c : cardManager.getCards()){
                        TitledPane tp = new TitledPane();
                        tp.setText(c.getName());
                        cardPanes.add(tp);
                    }
                    
                    accordionPaneCards.getPanes().addAll(cardPanes);
                    
                    btnLoadFromAPI.setDisable(false);
                    btnLoadFromFile.setDisable(false);
                    btnWriteToFile.setDisable(false);
                    progressIndicator.setVisible(false);
                }
            });
        } catch (Exception e) {
        }
    }
}
