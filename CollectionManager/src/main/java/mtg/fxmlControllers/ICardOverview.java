/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtg.fxmlControllers;

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

    public void setCurrentlyShownCardPanes(List<?> CurrentlyShownCardPanes);

    public Node CardUIConverter();

    public Node addCardComponents();
    
    public void filterCards(String filter);

    public void refreshCardPages(int beginIndex, List<?> cardPanes);

    public void fillFieldData(Node node);

    public void clearFieldData(Node node);
}