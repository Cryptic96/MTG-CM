package mtg.fxmlControllers.viewTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import javafx.scene.Node;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Node addCardComponents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void filterCards(String filter) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refreshCardPages(int beginIndex, List<?> cardPanes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // <editor-fold defaultstate="collapsed" desc="Field Data Updates">
    @Override
    public void fillFieldData(Node node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clearFieldData(Node node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    // </editor-fold>
}