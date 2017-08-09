package mtg.fxmlControllers.viewTypes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import mtg.enums.E_PropertyName;
import mtg.managers.CardManager;
import mtg.managers.CardProperty;

public abstract class CardOverviewView {

    protected final ExecutorService threadPool;
    
    protected int cardStartIndex = 0;
    protected CardManager cardManager;
    protected Image defaultImage;
    private final TableColumn propertyCol = new TableColumn("Property");
    
    // <editor-fold defaultstate="collapsed" desc="FXML Items">
    // Page Controls
    protected Label lblPage;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public int getCardStartIndex(){
        return this.cardStartIndex;
    }
    
    public void setCardStartIndex(int index){
        this.cardStartIndex = index;
    }

    public Image getDefaultImage() {
        return defaultImage;
    }
    // </editor-fold>
    
    public CardOverviewView(ExecutorService threadPool, CardManager cardManager, Node[] nodes) {
        this.threadPool = threadPool;
        this.cardManager = cardManager;
        
        this.lblPage = (Label) nodes[0];
        
        this.propertyCol.setMinWidth(100);
        this.propertyCol.setCellValueFactory(new PropertyValueFactory<>("propName"));
        
        try {
            this.defaultImage = new Image(new FileInputStream("./files/default_image.png"));
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }
    
    protected Pane createImagePane(int w, int h, int x, int y) {
        Pane imagePane = new Pane();
        imagePane.setPrefWidth(220);
        imagePane.setPrefHeight(310);
        imagePane.setLayoutX(15);
        imagePane.setLayoutY(15);
        imagePane.setStyle("-fx-border-color: #000");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(220);
        imageView.setFitHeight(310);
        imageView.setLayoutX(0);
        imageView.setLayoutY(0);
        imagePane.getChildren().add(imageView);

        return imagePane;
    }

    protected TableView<CardProperty> createTableView(int h, int x, int y) {
        TableView<CardProperty> table = new TableView();
        table.setPrefHeight(310);
        table.setTranslateX(250);
        table.setTranslateY(15);
        table.setStyle("-fx-border-color: #000");

        // make observable list to add data to tableview
        TableColumn valueCol = new TableColumn("Value");

        Collection<CardProperty> templist = new ArrayList();
        for (E_PropertyName p : E_PropertyName.values()) {
            templist.add(new CardProperty(p, null));
        }
        ObservableList<CardProperty> data
                = FXCollections.observableArrayList(templist);

        valueCol.setMinWidth(500);
        valueCol.setCellValueFactory(new PropertyValueFactory<>("propValue"));

        table.setItems(data);
        table.getColumns().addAll(propertyCol, valueCol);
        
        return table;
    }
}
