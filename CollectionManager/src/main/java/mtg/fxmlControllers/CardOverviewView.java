package mtg.fxmlControllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import mtg.managers.CardManager;

public abstract class CardOverviewView {

    protected final ExecutorService threadPool;
    
    protected int cardStartIndex = 0;
    protected CardManager cardManager;
    protected Image defaultImage;
    
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
        
        try {
            this.defaultImage = new Image(new FileInputStream("./files/default_image.png"));
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }
}
