package mtg.data;

import io.magicthegathering.javasdk.resource.Card;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *
 * @author theun
 */
public class CardItem {
    
    private final Card card;
    private Pane cardPane;
    private Image cardImage;
    
    public Pane getCardPane(){
        return cardPane;
    }

    public CardItem(Card card) {
        this.card = card;
        CardUIConverter();
    }
    
    private void CardUIConverter() {
            cardPane = new Pane();
            cardPane.setUserData(card);
            addCardComponents(cardPane);
    }

    private void addCardComponents(Node node) {
        Pane cardpane = (Pane) node;
        Card c = (Card) cardpane.getUserData();
        cardpane.setPrefWidth(220);
        cardpane.setPrefHeight(335);
        cardpane.setStyle("-fx-border-color: #000");

        // imagepane
        Pane imagePane = new Pane();
        imagePane.setPrefWidth(220);
        imagePane.setPrefHeight(310);
        imagePane.setLayoutX(0);
        imagePane.setLayoutY(0);
        imagePane.setStyle("-fx-border-color: #000");

        // imageView
        ImageView imageView = new ImageView();
        imageView.setFitWidth(220);
        imageView.setFitHeight(310);
        imageView.setLayoutX(0);
        imageView.setLayoutY(0);
        imagePane.getChildren().add(imageView);
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
}
