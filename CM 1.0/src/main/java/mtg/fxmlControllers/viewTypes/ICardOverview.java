/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtg.fxmlControllers.viewTypes;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.image.Image;

/**
 *
 * @author Theun Schut
 */
public interface ICardOverview {

    public int getCardStartIndex();

    public void setCardStartIndex(int index);

    public Image getDefaultImage();

    public List<?> getAllCardPanes();

    public void setAllCardPanes(List<?> allCardPanes);

    public List<?> getFilterCardPanes();

    public void setFilterCardPanes(List<?> filterCardPanes);

    public List<?> getCurrentlyShownCardPanes();

    public void setCurrentlyShownCardPanes(List<?> currentlyShownCardPanes);
    
    public Node getCardUINode();

    public void CardUIConverter();

    public void addCardComponents(Node node);
    
    public void filterCards(String filter);

    public void refreshCardPages(int beginIndex, List<?> cardPanes);
}
