package mtg.fxmlControllers;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import mtg.enums.E_PropertyName;
import mtg.managers.CardManager;
import mtg.managers.CardProperty;

public class CardOverviewList extends CardOverviewView implements ICardOverview {

    private List<TitledPane> allCardPanes;
    private List<TitledPane> filterCardPanes;
    private List<TitledPane> CurrentlyShownCardPanes;
    private final TableColumn propertyCol = new TableColumn("Property");

    // <editor-fold defaultstate="collapsed" desc="FXML Items">
    // Card View
    private Accordion accordionCards;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters & Setters">
    @Override
    public List<TitledPane> getAllCardPanes() {
        return (List<TitledPane>) allCardPanes;
    }

    @Override
    public void setAllCardPanes(List<?> allCardPanes) {
        this.allCardPanes = (List<TitledPane>) allCardPanes;
    }

    @Override
    public List<TitledPane> getFilterCardPanes() {
        return (List<TitledPane>) filterCardPanes;
    }

    @Override
    public void setFilterCardPanes(List<?> filterCardPanes) {
        this.filterCardPanes = (List<TitledPane>) filterCardPanes;
    }

    @Override
    public List<TitledPane> getCurrentlyShownCardPanes() {
        return (List<TitledPane>) CurrentlyShownCardPanes;
    }

    @Override
    public void setCurrentlyShownCardPanes(List<?> CurrentlyShownCardPanes) {
        this.CurrentlyShownCardPanes = (List<TitledPane>) CurrentlyShownCardPanes;
    }
    // </editor-fold>

    public CardOverviewList(ExecutorService threadPool, CardManager cardManager, Node[] nodes) {
        super(threadPool, cardManager, nodes);
        this.allCardPanes = new ArrayList<>();
        this.filterCardPanes = new ArrayList<>();
        this.CurrentlyShownCardPanes = new ArrayList<>();
        this.propertyCol.setMinWidth(100);
        this.propertyCol.setCellValueFactory(
                new PropertyValueFactory<>("propName"));
    }
    
    @Override
    public Node CardUIConverter() {
        this.accordionCards = new Accordion();
        for (Card c : cardManager.getCards()) {
            final TitledPane tp = new TitledPane();
            tp.setUserData(c);
            tp.setText(c.getName());
            tp.setContent((Pane) addCardComponents());
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
            allCardPanes.add(tp);
        }
        return this.accordionCards;
    }

    @Override
    public void filterCards(String filter){
        for (TitledPane tp : this.allCardPanes) {
            if (tp.getText().toUpperCase().contains(filter.toUpperCase())) {
                this.filterCardPanes.add(tp);
            }
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Card Components">
    @Override
    public Node addCardComponents() {
        final Pane pane = new Pane();
        pane.setPrefHeight(341);
        pane.setStyle("-fx-border-color: #000; -fx-background-color: #d3d3d3");

        // image pane
        Pane imagePane = new Pane();
        imagePane.setPrefWidth(220);
        imagePane.setPrefHeight(310);
        imagePane.setLayoutX(15);
        imagePane.setLayoutY(15);
        imagePane.setStyle("-fx-border-color: #000");
        pane.getChildren().add(imagePane);

        // imageview
        ImageView imageView = new ImageView();
        imageView.setFitWidth(220);
        imageView.setFitHeight(310);
        imageView.setLayoutX(0);
        imageView.setLayoutY(0);
        imagePane.getChildren().add(imageView);

        // tableview
        final TableView<CardProperty> table = new TableView();
        table.setPrefHeight(310);
        table.setTranslateX(250);
        table.setTranslateY(15);
        table.setStyle("-fx-border-color: #000");
        pane.getChildren().add(table);

        // make observable list to add data to tableview
        final TableColumn valueCol = new TableColumn("Value");
        pane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                table.setPrefWidth(pane.getWidth() - 265);
                //valueCol.setPrefWidth(table.getWidth() - propertyCol.getWidth() - 20);
            }
        });

        Collection<CardProperty> templist = new ArrayList();
        for (E_PropertyName p : E_PropertyName.values()) {
            templist.add(new CardProperty(p, null));
        }
        final ObservableList<CardProperty> data
                = FXCollections.observableArrayList(templist);

        valueCol.setMinWidth(500);

        valueCol.setCellValueFactory(new PropertyValueFactory<>("propValue"));

        table.setItems(data);
        table.getColumns().addAll(propertyCol, valueCol);

        return pane;
    }
    // </editor-fold>

    @Override
    public void refreshCardPages(int beginIndex, List<?> cardPanes) {
        System.out.println("Refreshing Page");
        try {
            int amount = 0;
            this.CurrentlyShownCardPanes.clear();
            accordionCards.getPanes().clear();

            for (int i = beginIndex; i < (beginIndex + 100); i++) {
                if (cardPanes.size() < (i + 1)) {
                    break;
                }
                TitledPane tp = (TitledPane) cardPanes.get(i);
                this.CurrentlyShownCardPanes.add(tp);
                accordionCards.getPanes().add(tp);
                amount = i - beginIndex;
            }

            // Set page label information
            lblPage.setText((cardStartIndex + 1) + " - "
                    + (cardStartIndex + amount + 1));
            
            // Thread image loading shizzle
            List<TitledPane> tempList = new ArrayList(this.CurrentlyShownCardPanes);
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
            while (this.tempCardPanes.containsAll(CurrentlyShownCardPanes)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }

            // destruct images in thread
            for (TitledPane tp : this.tempCardPanes) {
                if (CurrentlyShownCardPanes.contains(tp)) {
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
