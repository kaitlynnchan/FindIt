package cmpt276.project.model;

import java.util.ArrayList;

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

    public void add(CardDeck cardDeck, ScoresManager scoresManager){
        cardDecks.add(cardDeck);
        scoresManagers.add(scoresManager);
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

    public int getCardDeckIndex(CardDeck cardDeck){

        for(int i = 0; i < cardDecks.size(); i++){
            if(cardDecks.get(i).getNumImages() == cardDeck.getNumImages()
                    && cardDecks.get(i).getCardDeckSize() == cardDeck.getCardDeckSize()){
                return i;
            }
        }
        return -1;
    }
}
