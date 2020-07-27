package cmpt276.project.model;

/**
 * CARD DECK CLASS
 * Stores a deck of cards, each card contains a specified number of images
 * The deck is represented by a 2D array
 * The card is represented in a row of the deck
 */
public class CardDeck {

    private int numCardsTotal;  // Number of cards in each game
    private int cardDeckSize;   // Size of the card size
    private int numImages;      // Number of images on each card
    private int cardIndex;      // Stores the index of the card that is on the top of the draw pile
    private int[] imageArr;     // Array of images, each index represents an specific fruit / vegetable
    private String[] wordArr;   // Array of words, each index represents an specific fruit / vegetable
    private Object[][] cards;   // Card array: first index indicates the card, second index indicates which images are on the card
    private Mode mode;          // Game mode

    private static CardDeck instance;

    private CardDeck() {}
    public static CardDeck getInstance(){
        if(instance == null){
            instance = new CardDeck();
        }
        return instance;
    }

    public int getNumCardsTotal() {
        return numCardsTotal;
    }

    public int getNumImages() {
        return numImages;
    }

    public int getCardIndex() {
        return cardIndex;
    }

    public int getCardDeckSize() {
        return cardDeckSize;
    }

    // Returns the image at the index on the selected card
    public Object getCardObject(int card, int index) {
        String[] split = ((String) cards[card][index]).split(",");
        String type = split[0];
        int i = Integer.parseInt(split[1]);
        if(type.equals("word")){
            return wordArr[i];
        } else{
            return imageArr[i];
        }
    }

    public void setNumImages(int numImages) {
        this.numImages = numImages;
    }

    public void setNumCardsTotal(int numCardsTotal) {
        this.numCardsTotal = numCardsTotal;
    }

    public void setImageArr(int[] imageArr) {
        this.imageArr = imageArr;
    }

    public void setCardDeckSize(int cardDeckSize) {
        this.cardDeckSize = cardDeckSize;
    }

    public void setWordArr(String[] wordArr) {
        this.wordArr = wordArr;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    // Set cardIndex to 1, since card[0] is put into the discard pile when the game starts
    public void setCardIndex() {
        this.cardIndex = 1;
    }

    public void incrementCardIndex() {
        this.cardIndex++;
    }

    public void populateCards(){
        cards = new Object[numCardsTotal][numImages];
        int row = 0;

        // Help taken from: https://www.ryadel.com/en/dobble-spot-it-algorithm-math-function-javascript/
        // Generate series from imageArr[0] to imageArr[numImages - 1]
        for (int i = 0; i <= numImages - 1; i++)  {
            int rand = checkRandom(0, 0);
            addValue(row, rand, 0, 0);

            for (int i2 = 1; i2 <= numImages - 1; i2++) {
                int indx = (numImages * i) - i + i2;
                rand = checkRandom(rand, i2);
                addValue(row, rand, i2, indx);
            }
            row++;
        }

        // Generate series from imageArr[numImages] to imageArr[numImages * (numImages - 1)]
        for (int i = 1; i <= numImages-1; i++) {
            for (int i2 = 1; i2 <= numImages - 1; i2++) {
                int rand = checkRandom(0, 0);
                addValue(row, rand, 0, i);

                for (int i3 = 1; i3 <= numImages - 1; i3++) {
                    int indx = numImages + (numImages - 1) * (i3 - 1)
                            + ( (i - 1) * (i3 - 1) + (i2 - 1) )
                            % (numImages - 1);
                    rand = checkRandom(rand, i3);
                    addValue(row, rand, i3, indx);
                }
                row++;
            }
        }

        shuffleCardsAndImages();
    }

    private void addValue(int row, int rand, int i, int indx) {
        if (rand == 0 || mode == Mode.NORMAL) {
            cards[row][i] = "image," + indx;
        } else if (rand == 1) {
            cards[row][i] = "word," + indx;
        }
    }

    private int checkRandom(int rand, int i2) {
        if (i2 == numImages - 1) {
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
        for(int i = 0; i < numCardsTotal; i++){
            int rand = (int) ((Math.random() * (numCardsTotal - i)) + i);
            Object[] tempCard = cards[i];
            cards[i] = cards[rand];
            cards[rand] = tempCard;

            for (int j = 0; j < numImages; j++) {
                rand = (int) ((Math.random() * (numImages - j)) + j);
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
        for(int i = 0; i < numImages; i++){

            String[] split = ((String) cards[cardIndex][imageIndex]).split(",");
            int idxDraw = Integer.parseInt(split[1]);

            split = ((String) cards[cardIndex - 1][i]).split(",");
            int idxDiscard = Integer.parseInt(split[1]);

            if(idxDraw == idxDiscard){
                return true;
            }
        }
        return false;
    }
}
