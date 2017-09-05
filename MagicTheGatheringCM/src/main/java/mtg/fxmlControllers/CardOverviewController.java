package mtg.fxmlControllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import mtg.data.CardItem;
import mtg.managers.CardManager;

public class CardOverviewController implements Initializable, IController {

    private CardManager cardManager;
    private List<CardItem> cardItem;

    // <editor-fold defaultstate="collapsed" desc="FXML Items">
    // Card List
    @FXML
    private ScrollPane cardOverviewPane;
    @FXML
    private Label lblPage;

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
    }

    // <editor-fold defaultstate="collapsed" desc="FXML Handlers">
    @FXML
    private void ButtonPreviousPage(ActionEvent event) {
//        if (viewMode.getCardStartIndex() == 0) {
//            return;
//        }
//
//        viewMode.setCardStartIndex(viewMode.getCardStartIndex() - 100);
//        CheckFilteredCards();
    }

    @FXML
    private void ButtonNextPage(ActionEvent event) {
//        if ((viewMode.getCardStartIndex() % 100) != 0) {
//            return;
//        }
//
//        viewMode.setCardStartIndex(viewMode.getCardStartIndex() + 100);
//        CheckFilteredCards();
    }

    @FXML
    private void SearchFieldActionEvent(ActionEvent event) {
//        viewMode.getFilterCardPanes().clear();
//        viewMode.setCardStartIndex(0);
//
//        if (tfSearchBar.getText().length() == 0) {
//            viewMode.refreshCardPages(
//                    viewMode.getCardStartIndex(),
//                    viewMode.getAllCardPanes());
//            return;
//        }
//
//        viewMode.filterCards(tfSearchBar.getText());
//        viewMode.refreshCardPages(
//                viewMode.getCardStartIndex(),
//                viewMode.getFilterCardPanes());

    }
    // </editor-fold>

    private void CheckFilteredCards() {
//        if (viewMode.getFilterCardPanes().isEmpty()) {
//            viewMode.refreshCardPages(
//                    viewMode.getCardStartIndex(),
//                    viewMode.getAllCardPanes());
//        } else {
//            viewMode.refreshCardPages(
//                    viewMode.getCardStartIndex(),
//                    viewMode.getFilterCardPanes());
//        }
    }

    private void refrehCardItemList(int startIndex) {
        
    }

    @Override
    public void refresh() {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                }
            });
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
