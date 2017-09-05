package mtg.data;

import io.magicthegathering.javasdk.resource.Card;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *
 * @author theun
 */
public class CardItem
{

    private final Card card;
    private final Pane cardPane;
    private ImageView cardImage;

    public Card getCard()
    {
        return card;
    }

    public Pane getCardPane()
    {
        return cardPane;
    }

    public ImageView getCardImage()
    {
        return cardImage;
    }

    public CardItem(Card card)
    {
        this.card = card;
        
        // Creating UI Node
        cardPane = new Pane();
        cardPane.setUserData(card);
        cardPane.setPrefWidth(220);
        cardPane.setPrefHeight(335);
        cardPane.setStyle("-fx-border-color: #000");

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
        cardPane.getChildren().add(imagePane);

        // label
        Label label = new Label();
        label.setPrefWidth(220);
        label.setPrefHeight(25);
        label.setLayoutX(0);
        label.setLayoutY(310);
        label.setAlignment(Pos.CENTER);
        label.setText(card.getName());
        cardPane.getChildren().add(label);
    }
}
