package mtg.fxmlControllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import mtg.data.CardItem;
import mtg.managers.CardManager;

public class CardOverviewController implements Initializable, IController
{

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

    public CardOverviewController()
    {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
    }

    // <editor-fold defaultstate="collapsed" desc="FXML Handlers">
    @FXML
    private void ButtonPreviousPage(ActionEvent event)
    {
//        if (viewMode.getCardStartIndex() == 0) {
//            return;
//        }
//
//        viewMode.setCardStartIndex(viewMode.getCardStartIndex() - 100);
//        CheckFilteredCards();
    }

    @FXML
    private void ButtonNextPage(ActionEvent event)
    {
//        if ((viewMode.getCardStartIndex() % 100) != 0) {
//            return;
//        }
//
//        viewMode.setCardStartIndex(viewMode.getCardStartIndex() + 100);
//        CheckFilteredCards();
    }

    @FXML
    private void SearchFieldActionEvent(ActionEvent event)
    {
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

    private void CheckFilteredCards()
    {
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

    private void refrehCardItemList(int startIndex)
    {

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
//                    System.out.println("Refreshing Page");
//                    try
//                    {
//                        int amount = 0;
//                        this.currentlyShownCardPanes.clear();
//                        mainViewTypeNode.getChildren().clear();
//
//                        double X = 15;
//                        double endX = 15;
//                        double Y = 0;
//
//                        for (int i = beginIndex; i < (beginIndex + 100); i++)
//                        {
//                            if (cardPanes.size() < (i + 1))
//                            {
//                                break;
//                            }
//                            Pane cardPane = (Pane) cardPanes.get(i);
//
//                            // calculating X & Y
//                            endX = X + cardPane.getPrefWidth() + 15;
//                            if (endX < this.mainViewTypeNode.getPrefWidth())
//                            {
//                                cardPane.setLayoutX(X);
//                                cardPane.setLayoutY(Y);
//
//                                X = endX;
//                            }
//                            else
//                            {
//                                X = 15;
//                                Y = Y + cardPane.getPrefHeight() + 15;
//
//                                cardPane.setLayoutX(X);
//                                cardPane.setLayoutY(Y);
//
//                                X = X + cardPane.getPrefWidth() + 15;
//                            }
//
//                            this.currentlyShownCardPanes.add(cardPane);
//                            this.mainViewTypeNode.getChildren().add(cardPane);
//                            amount = i - beginIndex;
//                        }
//
//                        // Set page label information
//                        lblPage.setText((cardStartIndex + 1) + " - "
//                                + (cardStartIndex + amount + 1));
//
//                        // Thread image loading shizzle
//                        List<Pane> tempList = new ArrayList<>(this.currentlyShownCardPanes);
//                        FillImages fillImages = new FillImages(tempList);
//                        EmptyImages emptyImages = new EmptyImages(tempList);
//
//                        threadPool.execute(fillImages);
//                        threadPool.execute(emptyImages);
//                    }
//                    catch (Exception e)
//                    {
//                        System.err.println(e);
//                    }
                }
            });
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }
}
