package mtg.fxmlControllers;

import io.magicthegathering.javasdk.resource.Card;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import mtg.enums.E_PropertyName;
import mtg.managers.CardManager;
import mtg.managers.CardProperty;

public class CardOverviewController implements Initializable, IController
{

    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private int cardStartIndex = 0;
    private CardManager cardManager;
    private List<TitledPane> allCardPanes;
    private List<TitledPane> filterCardPanes;
    private List<TitledPane> CurrentlyShownCardPanes;
    private Image defaultImage;
    private final TableColumn propertyCol = new TableColumn("Property");
    private static final String DEFAULT_STRING = "No Data yet";

    // <editor-fold defaultstate="collapsed" desc="FXML Items">
    // Buttons 
    @FXML
    private MenuItem btnLoadFromAPI;
    @FXML
    private MenuItem btnLoadFromFile;
    @FXML
    private MenuItem btnWriteToFile;

    // Loading Screen
    @FXML
    private ProgressIndicator progressIndicator;

    // Filter Options
    @FXML
    private TextField tfSearchBar;

    // Card View
    @FXML
    private Accordion accordionCards;
    @FXML
    private ImageView cardImage;

    // Page Controls
    @FXML
    private Label lblPage;
    // </editor-fold>

    public CardOverviewController()
    {
        try
        {
            this.defaultImage = new Image(new FileInputStream("./files/default_image.png"));
        } catch (FileNotFoundException ex)
        {
            System.err.println(ex);
        }
        propertyCol.setMinWidth(100);
        propertyCol.setCellValueFactory(
                new PropertyValueFactory<CardProperty, String>("propName"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.cardManager = new CardManager(this);
        this.allCardPanes = new ArrayList<>();
        this.filterCardPanes = new ArrayList<>();
        this.CurrentlyShownCardPanes = new ArrayList<>();

        // fill cards at the beginning
        cardManager.ReadAllCardsFromFile();
    }

    // <editor-fold defaultstate="collapsed" desc="FXML Handlers">
    @FXML
    private void ButtonLoadFromAPI(ActionEvent event)
    {
        btnLoadFromAPI.setDisable(true);
        btnLoadFromFile.setDisable(true);
        btnWriteToFile.setDisable(true);
        //progressIndicator.setVisible(true);

        cardManager.CallAllCardsFromAPI();
    }

    @FXML
    private void ButtonLoadFromFile(ActionEvent event)
    {
        btnLoadFromAPI.setDisable(true);
        btnLoadFromFile.setDisable(true);
        btnWriteToFile.setDisable(true);
        //progressIndicator.setVisible(true);

        cardManager.ReadAllCardsFromFile();
    }

    @FXML
    private void ButtonWriteToFile(ActionEvent event)
    {
        btnLoadFromAPI.setDisable(true);
        btnLoadFromFile.setDisable(true);
        btnWriteToFile.setDisable(true);
        //progressIndicator.setVisible(true);

        cardManager.WriteAllCardsToFile();
    }

    @FXML
    private void ButtonPreviousPage(ActionEvent event)
    {
        if (cardStartIndex == 0)
        {
            return;
        }

        cardStartIndex = cardStartIndex - 100;
        ButtonPageCheckFilteredCards();
    }

    @FXML
    private void ButtonNextPage(ActionEvent event)
    {
        if ((cardStartIndex % 100) != 0)
        {
            return;
        }

        cardStartIndex = cardStartIndex + 100;
        ButtonPageCheckFilteredCards();
    }

    private void ButtonPageCheckFilteredCards()
    {
        if (this.filterCardPanes.isEmpty())
        {
            refreshCardPages(cardStartIndex, this.allCardPanes);
        } else
        {
            refreshCardPages(cardStartIndex, this.filterCardPanes);
        }
    }

    @FXML
    private void SearchFieldActionEvent(ActionEvent event)
    {
        this.filterCardPanes.clear();
        cardStartIndex = 0;

        if (tfSearchBar.getText().length() == 0)
        {
            refreshCardPages(cardStartIndex, this.allCardPanes);
            return;
        }

        for (TitledPane tp : this.allCardPanes)
        {
            if (tp.getText().toUpperCase().contains(tfSearchBar.getText().toUpperCase()))
            {
                this.filterCardPanes.add(tp);
            }
        }

        refreshCardPages(cardStartIndex, this.filterCardPanes);
    }
    // </editor-fold>

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
                    cardStartIndex = 0;
                    CardToTitlePaneConverter();
                    refreshCardPages(cardStartIndex, allCardPanes);

                    btnLoadFromAPI.setDisable(false);
                    btnLoadFromFile.setDisable(false);
                    btnWriteToFile.setDisable(false);
//                    progressIndicator.setVisible(false);
                }
            });
        } catch (Exception e)
        {
            System.err.println(e);
        }
    }

    /**
     * <p>
     * This method creates for every card in the card List a TitledPane Every
     * pane has a height property observer for loading card images.
     * </p>
     *
     * <p>
     * This Method is only called once after a card List input:
     * <ul>
     * <li>Reload from API</li>
     * <li>Reload from File</li>
     * </ul>
     * </p>
     */
    private void CardToTitlePaneConverter()
    {
        for (Card c : cardManager.getCards())
        {
            final TitledPane tp = new TitledPane();
            tp.setUserData(c);
            tp.setText(c.getName());
            tp.setContent(addCardComponents());
            tp.heightProperty().addListener(new ChangeListener<Number>()
            {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
                {
                    Card c = (Card) tp.getUserData();
                    Pane pane = (Pane) tp.getContent();
                    Pane imagePane = (Pane) pane.getChildren().get(0);
                    ImageView imageView = (ImageView) imagePane.getChildren().get(0);

                    if (imageView.getImage() == null && tp.getHeight() > 50)
                    {
                        fillFieldData(tp);
                        if (c.getImageUrl() == null)
                        {
                            imageView.setImage(defaultImage);
                        } else
                        {
                            imageView.setImage(new Image(c.getImageUrl()));
                        }
                    }
                }
            });
            allCardPanes.add(tp);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Create methods for nodes">
    private Pane addCardComponents()
    {
        final Pane pane = new Pane();
        pane.setPrefHeight(341);
        pane.setStyle("-fx-border-color: #000; -fx-background-color: #d3d3d3");

        // image pane
        Pane imagePane = createImagePane();
        pane.getChildren().add(imagePane);

        // imageview
        ImageView imageView = createImageView();
        imagePane.getChildren().add(imageView);

        // panel for labels for the card fields
        //final ScrollPane labelScrollPane = createScrollPane();
        // put table in scrollpane
        // make observable list to add data
        final TableView<CardProperty> table = new TableView();
        table.setTranslateX(260);
        table.setTranslateY(8);
        table.setPrefHeight(328);

        pane.getChildren().add(table);
        final TableColumn valueCol = new TableColumn("Value");
        pane.widthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue)
            {
                table.setPrefWidth(pane.getWidth() - 270);
                //valueCol.setPrefWidth(table.getWidth() - propertyCol.getWidth() - 20);
            }
        });

        Collection<CardProperty> templist = new ArrayList();
        for (E_PropertyName p : E_PropertyName.values())
        {
            templist.add(new CardProperty(p, null));
        }
        final ObservableList<CardProperty> data
                = FXCollections.observableArrayList(templist);

        valueCol.setMinWidth(500);

        valueCol.setCellValueFactory(
                new PropertyValueFactory<CardProperty, String>("propValue"));

        table.setItems(data);
        table.getColumns().addAll(propertyCol, valueCol);

        return pane;
    }

    private Pane createImagePane()
    {
        Pane imagePane = new Pane();
        imagePane.setPrefSize(226, 311);
        imagePane.setLayoutX(15);
        imagePane.setLayoutY(15);
        imagePane.setStyle("-fx-border-color: #000");
        return imagePane;
    }

    private ImageView createImageView()
    {
        ImageView imageview = new ImageView();
        imageview.setFitWidth(226);
        imageview.setFitHeight(311);
        imageview.setLayoutX(0);
        imageview.setLayoutY(0);
        return imageview;
    }

    private ScrollPane createScrollPane()
    {
        final ScrollPane labelScrollPane = new ScrollPane();
        labelScrollPane.setPrefSize(600, 311);
        labelScrollPane.setLayoutX(256);
        labelScrollPane.setLayoutY(15);
        labelScrollPane.setStyle("-fx-border-color: #000");
        return labelScrollPane;
    }
    // </editor-fold>

    private void refreshCardPages(int beginIndex, List<TitledPane> cardPanes)
    {
        int amount = 0;
        this.CurrentlyShownCardPanes.clear();
        accordionCards.getPanes().clear();

        for (int i = beginIndex; i < (beginIndex + 100); i++)
        {
            if (cardPanes.size() < (i + 1))
            {
                break;
            }
            TitledPane tp = cardPanes.get(i);
            this.CurrentlyShownCardPanes.add(tp);
            accordionCards.getPanes().add(tp);
            amount = i - beginIndex;
        }

        lblPage.setText((cardStartIndex + 1) + " - " + (cardStartIndex + amount + 1));

        // Thread image loading shizzle
        List<TitledPane> tempList = new ArrayList(this.CurrentlyShownCardPanes);
        FillImages fillImages = new FillImages(tempList);
        EmptyImages emptyImages = new EmptyImages(tempList);

        threadPool.execute(fillImages);
        threadPool.execute(emptyImages);
    }

    // <editor-fold defaultstate="collapsed" desc="Field Data Updates">
    private void fillFieldData(TitledPane tp)
    {
        Pane pane = (Pane) tp.getContent();

        final TableView table = ((TableView) pane.getChildren().get(1));

        Card c = (Card) tp.getUserData();

        Collection<CardProperty> templist = new ArrayList();
        for (E_PropertyName p : E_PropertyName.values())
        {
            templist.add(CardProperty.getCardProperty(c, p));
        }
        final ObservableList<CardProperty> data
                = FXCollections.observableArrayList(templist);

        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                table.setItems(data);
            }
        });

    }

    private void clearFieldData(TitledPane tp)
    {
        Pane pane = (Pane) tp.getContent();
        Pane labelPane = (Pane) ((ScrollPane) pane.getChildren().get(1)).getContent();
        Label label = (Label) labelPane.getChildren().get(0);
        label.setText(DEFAULT_STRING);

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Runnable Classes">
    class FillImages implements Runnable
    {

        List<TitledPane> tempCardPanes;

        public FillImages(List<TitledPane> tempCardPanes)
        {
            this.tempCardPanes = tempCardPanes;
        }

        @Override
        public void run()
        {
            for (TitledPane tp : this.tempCardPanes)
            {
                if (!this.tempCardPanes.contains(tp))
                {
                    break;
                }

                // fill next image
                Pane pane = (Pane) tp.getContent();
                Pane imagePane = (Pane) pane.getChildren().get(0);
                ImageView imageView = (ImageView) imagePane.getChildren().get(0);
                fillFieldData(tp);
                Card c = (Card) tp.getUserData();
                if (imageView.getImage() == null && c.getImageUrl() != null)
                {
                    imageView.setImage(new Image(c.getImageUrl()));
                }
            }
        }
    }

    class EmptyImages implements Runnable
    {

        List<TitledPane> tempCardPanes;

        public EmptyImages(List<TitledPane> tempCardPanes)
        {
            this.tempCardPanes = tempCardPanes;
        }

        @Override
        public void run()
        {
            // Wait for User to change page
            while (this.tempCardPanes.containsAll(CurrentlyShownCardPanes))
            {
                try
                {
                    Thread.sleep(100);
                } catch (InterruptedException e)
                {
                }
            }

            // destruct images in thread
            for (TitledPane tp : this.tempCardPanes)
            {
                if (CurrentlyShownCardPanes.contains(tp))
                {
                    continue;
                }
                // empty all images
                Pane pane = (Pane) tp.getContent();
                Pane imagePane = (Pane) pane.getChildren().get(0);
                ImageView imageView = (ImageView) imagePane.getChildren().get(0);
                clearFieldData(tp);
                if (imageView.getImage() != null)
                {
                    imageView.setImage(null);
                }
            }
        }
    }
// </editor-fold>
}
