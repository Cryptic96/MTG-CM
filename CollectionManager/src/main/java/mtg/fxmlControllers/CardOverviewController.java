/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtg.fxmlControllers;

import io.magicthegathering.javasdk.resource.Card;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import mtg.managers.CardManager;

/**
 * FXML Controller class
 *
 * @author Theun Schut
 */
public class CardOverviewController implements Initializable, IController {

    private CardManager cardManager;

    // Buttons 
    @FXML
    private Button btnLoadFromAPI;
    @FXML
    private Button btnLoadFromFile;
    @FXML
    private Button btnWriteToFile;
    @FXML
    private ProgressIndicator progressIndicator;

    // TableColumns
    @FXML
    private TableView<Card> tableViewCards;
    @FXML
    private TableColumn<Card, String> nameColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.cardManager = new CardManager(this);
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
