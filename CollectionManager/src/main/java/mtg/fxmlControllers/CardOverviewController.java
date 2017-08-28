package mtg.fxmlControllers;

import mtg.fxmlControllers.viewTypes.ICardOverview;
import mtg.fxmlControllers.viewTypes.CardOverviewList;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import mtg.fxmlControllers.viewTypes.CardOverviewGrid;
import mtg.managers.CardManager;

public class CardOverviewController implements Initializable, IController {

    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    
    private CardManager cardManager;
    private ICardOverview viewMode;
    
    // <editor-fold defaultstate="collapsed" desc="FXML Items">
    // Buttons 
    @FXML
    private MenuItem btnLoadFromAPI;
    @FXML
    private MenuItem btnLoadFromFile;
    @FXML
    private MenuItem btnWriteToFile;

    // Card List
    @FXML
    private ScrollPane cardOverviewPane;
    @FXML
    private Label lblPage;
    
    // Loading Screen
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label lblProgress;

    // Filter Options
    @FXML
    private TextField tfSearchBar;
    // </editor-fold>

    public CardOverviewController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.lblPage.setId("lblPage");
        Node[] nodes = {this.lblPage};
        
        this.cardManager = new CardManager(this);
        
        // Check radiobuttons for view mode
        // TODO: create if-statement for radiobuttons
        
        
        this.viewMode = new CardOverviewList(threadPool, cardManager, nodes);
//        this.viewMode = new CardOverviewGrid(threadPool, cardManager, nodes);
        
        // fill cards at the beginning
        cardManager.ReadAllCardsFromFile();
    }

    // <editor-fold defaultstate="collapsed" desc="FXML Handlers">
    @FXML
    private void ButtonLoadFromAPI(ActionEvent event) {
        btnLoadFromAPI.setDisable(true);
        btnLoadFromFile.setDisable(true);
        btnWriteToFile.setDisable(true);

        cardManager.CallAllCardsFromAPI();
    }

    @FXML
    private void ButtonLoadFromFile(ActionEvent event) {
        btnLoadFromAPI.setDisable(true);
        btnLoadFromFile.setDisable(true);
        btnWriteToFile.setDisable(true);

        cardManager.ReadAllCardsFromFile();
    }

    @FXML
    private void ButtonWriteToFile(ActionEvent event) {
        btnLoadFromAPI.setDisable(true);
        btnLoadFromFile.setDisable(true);
        btnWriteToFile.setDisable(true);

        cardManager.WriteAllCardsToFile();
    }

    @FXML
    private void ButtonPreviousPage(ActionEvent event) {
        if (viewMode.getCardStartIndex() == 0) {
            return;
        }

        viewMode.setCardStartIndex(viewMode.getCardStartIndex() - 100);
        CheckFilteredCards();
    }

    @FXML
    private void ButtonNextPage(ActionEvent event) {
        if ((viewMode.getCardStartIndex() % 100) != 0) {
            return;
        }

        viewMode.setCardStartIndex(viewMode.getCardStartIndex() + 100);
        CheckFilteredCards();
    }

    @FXML
    private void SearchFieldActionEvent(ActionEvent event) {
        viewMode.getFilterCardPanes().clear();
        viewMode.setCardStartIndex(0);

        if (tfSearchBar.getText().length() == 0) {
            viewMode.refreshCardPages(
                    viewMode.getCardStartIndex(),
                    viewMode.getAllCardPanes());
            return;
        }

        viewMode.filterCards(tfSearchBar.getText());
        viewMode.refreshCardPages(
                viewMode.getCardStartIndex(),
                viewMode.getFilterCardPanes());
        
    }
    // </editor-fold>

    private void CheckFilteredCards() {
        if (viewMode.getFilterCardPanes().isEmpty()) {
            viewMode.refreshCardPages(
                    viewMode.getCardStartIndex(),
                    viewMode.getAllCardPanes());
        } else {
            viewMode.refreshCardPages(
                    viewMode.getCardStartIndex(),
                    viewMode.getFilterCardPanes());
        }
    }
    
    @Override
    public void refresh() {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    viewMode.setCardStartIndex(0);
                    cardOverviewPane.setContent(viewMode.CardUIConverter());
                    viewMode.refreshCardPages(
                            viewMode.getCardStartIndex(), 
                            viewMode.getAllCardPanes());

                    btnLoadFromAPI.setDisable(false);
                    btnLoadFromFile.setDisable(false);
                    btnWriteToFile.setDisable(false);
                }
            });
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}