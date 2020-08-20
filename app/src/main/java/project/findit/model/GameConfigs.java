package project.findit.model;

import java.util.ArrayList;

/**
 * GAME CONFIGS CLASS
 * Stores a collection of cardDecks and scoresManagers
 */
public class GameConfigs {
    private ArrayList<CardDeck> cardDecks = new ArrayList<>();
    private ArrayList<ScoresManager> scoresManagers = new ArrayList<>();

    // Singleton implementation
    private static GameConfigs instance;
    private GameConfigs() {}
    public static GameConfigs getInstance(){
        if(instance == null){
            instance = new GameConfigs();
        }
        return instance;
    }

    public ArrayList<CardDeck> getCardDecks(){
        return cardDecks;
    }

    public ArrayList<ScoresManager> getScoresManagers(){
        return scoresManagers;
    }

    public ScoresManager getScoreManager(int index){
        return scoresManagers.get(index);
    }
    public void setCardDecks(ArrayList<CardDeck> cardDecks) {
        this.cardDecks = cardDecks;
    }

    public void setScoresManagers(ArrayList<ScoresManager> scoresManagers) {
        this.scoresManagers = scoresManagers;
    }

    public void add(CardDeck cardDeck, ScoresManager scoresManager){
        cardDecks.add(cardDeck);
        scoresManagers.add(scoresManager);
    }

    public int getCardDeckIndex(CardDeck cardDeck){
        for(int i = 0; i < cardDecks.size(); i++){
            if(cardDecks.get(i).getNumImagesPerCard() == cardDeck.getNumImagesPerCard()
                    && cardDecks.get(i).getNumCards() == cardDeck.getNumCards()){
                return i;
            }
        }
        return -1;
    }

    public int getCardDeckIndex(int numImagesPerCard, int numCards){
        for(int i = 0; i < cardDecks.size(); i++){
            if(cardDecks.get(i).getNumImagesPerCard() == numImagesPerCard
                    && cardDecks.get(i).getNumCards() == numCards){
                return i;
            }
        }
        return -1;
    }
}
