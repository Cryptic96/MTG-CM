package mtg.fxmlControllers.viewTypes;

import io.magicthegathering.javasdk.resource.Card;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import mtg.managers.CardManager;

public class CardOverviewGrid extends CardOverviewView implements ICardOverview {

    private List<Pane> allCardPanes;
    private List<Pane> filterCardPanes;
    private List<Pane> currentlyShownCardPanes;

    // <editor-fold defaultstate="collapsed" desc="FXML Items">
    // Card View, this is the main Node for this view type
    private Pane mainViewTypeNode;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters & Setters">
    @Override
    public List<Pane> getAllCardPanes() {
        return (List<Pane>) allCardPanes;
    }

    @Override
    public void setAllCardPanes(List<?> allCardPanes) {
        this.allCardPanes = (List<Pane>) allCardPanes;
    }

    @Override
    public List<Pane> getFilterCardPanes() {
        return (List<Pane>) this.filterCardPanes;
    }

    @Override
    public void setFilterCardPanes(List<?> filterCardPanes) {
        this.filterCardPanes = (List<Pane>) filterCardPanes;
    }

    @Override
    public List<Pane> getCurrentlyShownCardPanes() {
        return (List<Pane>) this.currentlyShownCardPanes;
    }

    @Override
    public void setCurrentlyShownCardPanes(List<?> currentlyShownCardPanes) {
        this.currentlyShownCardPanes = (List<Pane>) currentlyShownCardPanes;
    }
    // </editor-fold>

    public CardOverviewGrid(ExecutorService threadPool, CardManager cardManager, Node[] nodes) {
        super(threadPool, cardManager, nodes);
        this.allCardPanes = new ArrayList<>();
        this.filterCardPanes = new ArrayList<>();
        this.currentlyShownCardPanes = new ArrayList<>();
    }

    @Override
    public Node CardUIConverter() {
        System.out.println("Creating GUI Nodes for every card");
        this.mainViewTypeNode = new Pane();
        this.mainViewTypeNode.setPrefWidth(980);
        for (Card c : cardManager.getCards()) {
            Pane cardPane = new Pane();
            cardPane.setUserData(c);
            addCardComponents(cardPane);
            this.allCardPanes.add(cardPane);
        }
        return this.mainViewTypeNode;
    }

    @Override
    public void addCardComponents(Node node) {
        Pane cardpane = (Pane) node;
        Card c = (Card) cardpane.getUserData();
        cardpane.setPrefWidth(220);
        cardpane.setPrefHeight(335);
        cardpane.setStyle("-fx-border-color: #000");

        // imagepane
        Pane imagePane = createImagePane(220, 310, 0, 0);
        cardpane.getChildren().add(imagePane);

        // label
        Label label = new Label();
        label.setPrefWidth(220);
        label.setPrefHeight(25);
        label.setLayoutX(0);
        label.setLayoutY(310);
        label.setAlignment(Pos.CENTER);
        label.setText(c.getName());
        cardpane.getChildren().add(label);
    }

    @Override
    public void filterCards(String filter) {
        Label l;
        for (Pane pane : this.allCardPanes) {
            l = (Label) pane.getChildren().get(1);
            if (l.getText().toUpperCase().contains(filter.toUpperCase())) {
                this.filterCardPanes.add(pane);
            }
        }
    }

    @Override
    public void refreshCardPages(int beginIndex, List<?> cardPanes) {
        System.out.println("Refreshing Page");
        try {
            int amount = 0;
            this.currentlyShownCardPanes.clear();
            mainViewTypeNode.getChildren().clear();

            double X = 15;
            double endX = 15;
            double Y = 0;

            for (int i = beginIndex; i < (beginIndex + 100); i++) {
                if (cardPanes.size() < (i + 1)) {
                    break;
                }
                Pane cardPane = (Pane) cardPanes.get(i);

                // calculating X & Y
                endX = X + cardPane.getPrefWidth() + 15;
                if(endX < this.mainViewTypeNode.getPrefWidth()){
                    cardPane.setLayoutX(X);
                    cardPane.setLayoutY(Y);
                    
                    X = endX;
                } else {
                    X = 15;
                    Y = Y + cardPane.getPrefHeight() + 15;
                    
                    cardPane.setLayoutX(X);
                    cardPane.setLayoutY(Y);
                    
                    X = X + cardPane.getPrefWidth() + 15;
                }
                
                this.currentlyShownCardPanes.add(cardPane);
                this.mainViewTypeNode.getChildren().add(cardPane);
                amount = i - beginIndex;
            }

            // Set page label information
            lblPage.setText((cardStartIndex + 1) + " - "
                    + (cardStartIndex + amount + 1));

            // Thread image loading shizzle
            List<Pane> tempList = new ArrayList<>(this.currentlyShownCardPanes);
            FillImages fillImages = new FillImages(tempList);
            EmptyImages emptyImages = new EmptyImages(tempList);
            
            threadPool.execute(fillImages);
            threadPool.execute(emptyImages);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Runnable Classes">
    class FillImages implements Runnable {

        List<Pane> tempCardPanes;

        public FillImages(List<Pane> tempCardPanes) {
            this.tempCardPanes = tempCardPanes;
        }

        @Override
        public void run() {
            for (Pane pane : this.tempCardPanes) {
                if (!this.tempCardPanes.contains(pane)) {
                    break;
                }

                // fill next image
                Pane imagePane = (Pane) pane.getChildren().get(0);
                ImageView imageView = (ImageView) imagePane.getChildren().get(0);
                Card c = (Card) pane.getUserData();
                if (imageView.getImage() == null && c.getImageUrl() != null) {
                    imageView.setImage(new Image(c.getImageUrl()));
                }
            }
        }
    }

    class EmptyImages implements Runnable {

        List<Pane> tempCardPanes;

        public EmptyImages(List<Pane> tempCardPanes) {
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
            for (Pane pane : this.tempCardPanes) {
                if (currentlyShownCardPanes.contains(pane)) {
                    continue;
                }
                // empty all images
                Pane imagePane = (Pane) pane.getChildren().get(0);
                ImageView imageView = (ImageView) imagePane.getChildren().get(0);
                if (imageView.getImage() != null) {
                    imageView.setImage(null);
                }
            }
        }
    }
// </editor-fold>
}
