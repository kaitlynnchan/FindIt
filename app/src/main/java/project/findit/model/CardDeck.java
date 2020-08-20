package project.findit.model;

import java.util.Random;

/**
 * CARD DECK CLASS
 * Creates and stores a deck of cards, each card with a specified number of objects
 */
public class CardDeck {

    private int numCards;
    private int numImagesPerCard;
    private Mode difficultyMode;
    private Object[][] cards;       // First [] indicates the card, second [][] indicates which images are on the card
    private int currentCardIndex;   // Index of the card that is on the top of the draw pile

    // Singleton implementation
    private static CardDeck instance;
    private CardDeck() {}
    public static CardDeck getInstance(){
        if(instance == null){
            instance = new CardDeck();
        }
        return instance;
    }

    public int getNumCards() {
        return numCards;
    }

    public void setNumCards(int numCards) {
        this.numCards = numCards;
    }

    public int getNumImagesPerCard() {
        return numImagesPerCard;
    }

    public void setNumImagesPerCard(int numImagesPerCard) {
        this.numImagesPerCard = numImagesPerCard;
    }

    public void setDifficultyMode(Mode difficultyMode) {
        this.difficultyMode = difficultyMode;
    }

    public int getCurrentCardIndex() {
        return currentCardIndex;
    }

    public void incrementCardIndex() {
        this.currentCardIndex++;
    }

    public void resetCurrentIndex() {
        this.currentCardIndex = 0;
    }

    // Returns the object at the index on the selected card
    public Object[] getCardObject(int card, int index) {
        String[] split = ((String) cards[card][index]).split(",");
        if(split.length == 4){
            Object[] temp = new Object[3];
            for(int i = 0; i < temp.length; i++){
                temp[i] = split[i + 1];
            }
            return temp;
        }
        return split;
    }

    public void populateCards(Object[] packArr){
        int numCardsTotal = (numImagesPerCard * numImagesPerCard) - numImagesPerCard + 1;
        cards = new Object[numCardsTotal][numImagesPerCard];
        int row = 0;

        // Help taken from:
        //  https://www.ryadel.com/en/dobble-spot-it-algorithm-math-function-javascript/
        // Generate series from
        //  packArr[0] to packArr[numImagesPerCard - 1]
        for (int i = 0; i <= numImagesPerCard - 1; i++)  {
            boolean isWord = checkRandom(false, 0);
            addValue(row, isWord, 0, 0, packArr);

            for (int i2 = 1; i2 <= numImagesPerCard - 1; i2++) {
                int indx = (numImagesPerCard * i) - i + i2;
                isWord = checkRandom(isWord, i2);
                addValue(row, isWord, i2, indx, packArr);
            }
            row++;
        }

        // Generate series from
        //  packArr[numImagesPerCard] to packArr[numImagesPerCard * (numImagesPerCard - 1)]
        for (int i = 1; i <= numImagesPerCard -1; i++) {
            for (int i2 = 1; i2 <= numImagesPerCard - 1; i2++) {
                boolean isWord = checkRandom(false, 0);
                addValue(row, isWord, 0, i, packArr);

                for (int i3 = 1; i3 <= numImagesPerCard - 1; i3++) {
                    int indx = numImagesPerCard + (numImagesPerCard - 1) * (i3 - 1)
                            + ( (i - 1) * (i3 - 1) + (i2 - 1) )
                            % (numImagesPerCard - 1);
                    isWord = checkRandom(isWord, i3);
                    addValue(row, isWord, i3, indx, packArr);
                }
                row++;
            }
        }

        shuffleCardsAndImages();
    }

    private boolean checkRandom(boolean isWord, int col) {
        if (col == numImagesPerCard - 1) {
            isWord = !isWord;
        } else {
            isWord = new Random().nextBoolean();
        }
        return isWord;
    }

    // If 0, the object is an image, 1 for word
    private void addValue(int row, boolean isWord, int col, int indx, Object[] packArr) {
        int rotate = 0;
        if(difficultyMode == Mode.NORMAL || difficultyMode == Mode.HARD){
            rotate = (int) (Math.random() * 360);
        }
        int scale = 1;
        if(difficultyMode == Mode.HARD){
            scale = (int) (Math.random() * 3) + 1;
        }
        
        if(packArr[indx].getClass() == String.class){
            String[] split = ((String) packArr[indx]).split(",");
            if (!isWord) {
                cards[row][col] = split[0] + "," + rotate + "," + scale;
            } else{
                cards[row][col] = packArr[indx] + "," + rotate + "," + scale;
            }
        } else{
            cards[row][col] = packArr[indx] + "," + rotate + "," + scale;
        }
    }

    public void shuffleCardsAndImages(){
        for(int i = 0; i < cards.length; i++){
            int rand = (int) ((Math.random() * (cards.length - i)) + i);
            Object[] tempCard = cards[i];
            cards[i] = cards[rand];
            cards[rand] = tempCard;

            for (int j = 0; j < numImagesPerCard; j++) {
                rand = (int) ((Math.random() * (numImagesPerCard - j)) + j);
                Object tempImage = cards[i][j];
                cards[i][j] = cards[i][rand];
                cards[i][rand] = tempImage;
            }
        }
    }

    // Checks if the picture that the user clicked on is present in the top card of the discard pile
    public boolean searchDiscardCard(int imageIndex) {
        for(int i = 0; i < numImagesPerCard; i++){
            Object drawValue;
            String[] split = ((String) cards[currentCardIndex][imageIndex]).split(",");
            drawValue = split[0];

            Object discardValue;
            split = ((String) cards[currentCardIndex - 1][i]).split(",");
            discardValue = split[0];

            if(drawValue.equals(discardValue)){
                return true;
            }
        }
        return false;
    }
}
