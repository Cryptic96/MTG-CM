package main;

import managers.CardCollectorMgr;

public class CollectionManager {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CardCollectorMgr ccmgr = new CardCollectorMgr();
        ccmgr.readAllCards();
        
        System.out.println(ccmgr.getCards().size());
    }
}
