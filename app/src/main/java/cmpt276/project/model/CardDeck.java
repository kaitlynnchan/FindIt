package cmpt276.project.model;

/**
 * CARD DECK CLASS
 * Stores a deck of cards, each card contains a specified number of images
 * The deck is represented by a 2D array
 * The card is represented in a row of the deck
 */
public class CardDeck {

    private int numCards;           // Number of cards in deck
    private int numImagesPerCard;   // Number of images on each card
    private int currentCardIndex;   // Stores the index of the card that is on the top of the draw pile
    private Mode difficultyMode;    // Specifies the difficulty of the game
    private Object[][] cards;       // Card array: first index indicates the card, second index indicates which images are on the card

    private static CardDeck instance;

    private CardDeck() {}
    public static CardDeck getInstance(){
        if(instance == null){
            instance = new CardDeck();
        }
        return instance;
    }

    public int getNumImagesPerCard() {
        return numImagesPerCard;
    }

    public int getCurrentCardIndex() {
        return currentCardIndex;
    }

    public int getNumCards() {
        return numCards;
    }

    // Returns the image at the index on the selected card
    public Object[] getCardObject(int card, int index) {
        String[] split = ((String) cards[card][index]).split(",");
        if(split.length == 4){
            Object[] temp = new Object[3];
            for(int i = 0; i < temp.length; i++){
                temp[i] = split[i+1];
            }
            return temp;
        }
        return split;
    }

    public void setNumImagesPerCard(int numImagesPerCard) {
        this.numImagesPerCard = numImagesPerCard;
    }

    public void setNumCards(int numCards) {
        this.numCards = numCards;
    }

    public void setDifficultyMode(Mode difficultyMode) {
        this.difficultyMode = difficultyMode;
    }

    // Set cardIndex to 1, since card[0] is put into the discard pile when the game starts
    public void setCardIndex() {
        this.currentCardIndex = 1;
    }

    public void incrementCardIndex() {
        this.currentCardIndex++;
    }

    public void populateCards(Object[] packArr){
        int numCardsTotal = (numImagesPerCard * numImagesPerCard) - numImagesPerCard + 1;
        cards = new Object[numCardsTotal][numImagesPerCard];
        int row = 0;

        // Help taken from: https://www.ryadel.com/en/dobble-spot-it-algorithm-math-function-javascript/
        // Generate series from imageArr[0] to imageArr[numImages - 1]
        for (int i = 0; i <= numImagesPerCard - 1; i++)  {
            int rand = checkRandom(0, 0);
            addValue(row, rand, 0, 0, packArr);

            for (int i2 = 1; i2 <= numImagesPerCard - 1; i2++) {
                int indx = (numImagesPerCard * i) - i + i2;
                rand = checkRandom(rand, i2);
                addValue(row, rand, i2, indx, packArr);
            }
            row++;
        }

        // Generate series from imageArr[numImages] to imageArr[numImages * (numImages - 1)]
        for (int i = 1; i <= numImagesPerCard -1; i++) {
            for (int i2 = 1; i2 <= numImagesPerCard - 1; i2++) {
                int rand = checkRandom(0, 0);
                addValue(row, rand, 0, i, packArr);

                for (int i3 = 1; i3 <= numImagesPerCard - 1; i3++) {
                    int indx = numImagesPerCard + (numImagesPerCard - 1) * (i3 - 1)
                            + ( (i - 1) * (i3 - 1) + (i2 - 1) )
                            % (numImagesPerCard - 1);
                    rand = checkRandom(rand, i3);
                    addValue(row, rand, i3, indx, packArr);
                }
                row++;
            }
        }

        shuffleCardsAndImages();
    }

    // Randomizes is the card object should be an image or word
    // If 0, the object is an image, 1 for word
    private void addValue(int row, int rand, int col, int indx, Object[] packArr) {
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
            if (rand == 0) {
                cards[row][col] = split[0] + "," + rotate + "," + scale;
            } else if (rand == 1) {
                cards[row][col] = packArr[indx] + "," + rotate + "," + scale;
            }
        } else{
            cards[row][col] = packArr[indx] + "," + rotate + "," + scale;
        }
    }

    private int checkRandom(int rand, int col) {
        if (col == numImagesPerCard - 1) {
            if (rand == 0) {
                rand = 1;
            } else {
                rand = 0;
            }
        } else {
            rand = (int) (Math.random() * 2);
        }
        return rand;
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
    // cardIndex - 1 refers to the card on top of the discard pile since the card in the discard pile is always one index behind the card in the draw pile
    // cardIndex refers to the index of the card on the top of the draw pile
    public boolean searchDiscardPile(int imageIndex) {
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
