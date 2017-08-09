package mtg.fxmlControllers.viewTypes;

import io.magicthegathering.javasdk.resource.Card;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import mtg.enums.E_PropertyName;
import mtg.managers.CardManager;
import mtg.managers.CardProperty;

public class CardOverviewList extends CardOverviewView implements ICardOverview {

    private List<TitledPane> allCardPanes;
    private List<TitledPane> filterCardPanes;
    private List<TitledPane> currentlyShownCardPanes;

    // <editor-fold defaultstate="collapsed" desc="FXML Items">
    // Card View, this is the main Node for this view type
    private Accordion mainViewTypeNode;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters & Setters">
    @Override
    public List<TitledPane> getAllCardPanes() {
        return (List<TitledPane>) this.allCardPanes;
    }

    @Override
    public void setAllCardPanes(List<?> allCardPanes) {
        this.allCardPanes = (List<TitledPane>) allCardPanes;
    }

    @Override
    public List<TitledPane> getFilterCardPanes() {
        return (List<TitledPane>) this.filterCardPanes;
    }

    @Override
    public void setFilterCardPanes(List<?> filterCardPanes) {
        this.filterCardPanes = (List<TitledPane>) filterCardPanes;
    }

    @Override
    public List<TitledPane> getCurrentlyShownCardPanes() {
        return (List<TitledPane>) this.currentlyShownCardPanes;
    }

    @Override
    public void setCurrentlyShownCardPanes(List<?> currentlyShownCardPanes) {
        this.currentlyShownCardPanes = (List<TitledPane>) currentlyShownCardPanes;
    }
    // </editor-fold>

    public CardOverviewList(ExecutorService threadPool, CardManager cardManager, Node[] nodes) {
        super(threadPool, cardManager, nodes);
        this.allCardPanes = new ArrayList<>();
        this.filterCardPanes = new ArrayList<>();
        this.currentlyShownCardPanes = new ArrayList<>();
    }

    @Override
    public Node CardUIConverter() {
        System.out.println("Creating GUI Nodes for every card");
        this.mainViewTypeNode = new Accordion();
        for (Card c : cardManager.getCards()) {
            final TitledPane tp = new TitledPane();
            tp.setUserData(c);
            tp.setText(c.getName());
            addCardComponents(tp);
            tp.heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    Card c = (Card) tp.getUserData();
                    Pane pane = (Pane) tp.getContent();
                    Pane imagePane = (Pane) pane.getChildren().get(0);
                    ImageView imageView = (ImageView) imagePane.getChildren().get(0);

                    if (imageView.getImage() == null && tp.getHeight() > 50) {
                        fillFieldData(tp);
                        if (c.getImageUrl() == null) {
                            imageView.setImage(defaultImage);
                        } else {
                            imageView.setImage(new Image(c.getImageUrl()));
                        }
                    }
                }
            });
            this.allCardPanes.add(tp);
        }
        return this.mainViewTypeNode;
    }

    @Override
    public void addCardComponents(Node node) {
        final Pane pane = new Pane();
        pane.setPrefHeight(340);
        pane.setStyle("-fx-border-color: #000; -fx-background-color: #d3d3d3");

        // image
        Pane imagePane = createImagePane(220, 310, 15, 15);
        pane.getChildren().add(imagePane);

        // tableview
        final TableView<CardProperty> tableView = createTableView(310, 250, 15);
        pane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                tableView.setPrefWidth(pane.getWidth() - 265);
                //valueCol.setPrefWidth(table.getWidth() - propertyCol.getWidth() - 20);
            }
        });
        pane.getChildren().add(tableView);
        ((TitledPane)node).setContent(pane);
    }

    @Override
    public void filterCards(String filter) {
        for (TitledPane tp : this.allCardPanes) {
            if (tp.getText().toUpperCase().contains(filter.toUpperCase())) {
                this.filterCardPanes.add(tp);
            }
        }
    }
    
    @Override
    public void refreshCardPages(int beginIndex, List<?> cardPanes) {
        System.out.println("Refreshing Page");
        try {
            int amount = 0;
            this.currentlyShownCardPanes.clear();
            mainViewTypeNode.getPanes().clear();

            for (int i = beginIndex; i < (beginIndex + 100); i++) {
                if (cardPanes.size() < (i + 1)) {
                    break;
                }
                TitledPane tp = (TitledPane) cardPanes.get(i);
                this.currentlyShownCardPanes.add(tp);
                this.mainViewTypeNode.getPanes().add(tp);
                amount = i - beginIndex;
            }

            // Set page label information
            lblPage.setText((cardStartIndex + 1) + " - "
                    + (cardStartIndex + amount + 1));

            // Thread image loading shizzle
            List<TitledPane> tempList = new ArrayList(this.currentlyShownCardPanes);
            FillImages fillImages = new FillImages(tempList);
            EmptyImages emptyImages = new EmptyImages(tempList);

            threadPool.execute(fillImages);
            threadPool.execute(emptyImages);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Field Data Updates">
    @Override
    public void fillFieldData(Node node) {
        TitledPane tp = (TitledPane) node;
        Pane pane = (Pane) tp.getContent();

        final TableView table = ((TableView) pane.getChildren().get(1));

        Card c = (Card) tp.getUserData();

        Collection<CardProperty> templist = new ArrayList();
        for (E_PropertyName p : E_PropertyName.values()) {
            templist.add(CardProperty.getCardProperty(c, p));
        }
        final ObservableList<CardProperty> data
                = FXCollections.observableArrayList(templist);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                table.setItems(data);
            }
        });
    }

    @Override
    public void clearFieldData(Node node) {

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Runnable Classes">
    class FillImages implements Runnable {

        List<TitledPane> tempCardPanes;

        public FillImages(List<TitledPane> tempCardPanes) {
            this.tempCardPanes = tempCardPanes;
        }

        @Override
        public void run() {
            for (TitledPane tp : this.tempCardPanes) {
                if (!this.tempCardPanes.contains(tp)) {
                    break;
                }

                // fill next image
                Pane pane = (Pane) tp.getContent();
                Pane imagePane = (Pane) pane.getChildren().get(0);
                ImageView imageView = (ImageView) imagePane.getChildren().get(0);
                fillFieldData(tp);
                Card c = (Card) tp.getUserData();
                if (imageView.getImage() == null && c.getImageUrl() != null) {
                    imageView.setImage(new Image(c.getImageUrl()));
                }
            }
        }
    }

    class EmptyImages implements Runnable {

        List<TitledPane> tempCardPanes;

        public EmptyImages(List<TitledPane> tempCardPanes) {
            this.tempCardPanes = tempCardPanes;
        }

        @Override
        public void run() {
            // Wait for User to change page
            while (this.tempCardPanes.containsAll(currentlyShownCardPanes)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }

            // destruct images in thread
            for (TitledPane tp : this.tempCardPanes) {
                if (currentlyShownCardPanes.contains(tp)) {
                    continue;
                }
                // empty all images
                Pane pane = (Pane) tp.getContent();
                Pane imagePane = (Pane) pane.getChildren().get(0);
                ImageView imageView = (ImageView) imagePane.getChildren().get(0);
                clearFieldData(tp);
                if (imageView.getImage() != null) {
                    imageView.setImage(null);
                }
            }
        }
    }
// </editor-fold>
}
