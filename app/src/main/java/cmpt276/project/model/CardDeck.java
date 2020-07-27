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
    private int[][] cards;      // Card array: first index indicates the card, second index indicates which images are on the card
    private int[] imageArr;     // Array of images, each index represents an specific fruit / vegetable

    private static CardDeck instance;

    private CardDeck() {}
    public static CardDeck getInstance(){
        if(instance == null){
            instance = new CardDeck();
        }
        return instance;
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
    public int getCardImage(int card, int index) {
        return cards[card][index];
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

    // Set cardIndex to 1, since card[0] is put into the discard pile when the game starts
    public void setCardIndex() {
        this.cardIndex = 1;
    }

    public void incrementCardIndex() {
        this.cardIndex++;
    }

    public void populateCards(){
        cards = new int[numCardsTotal][numImages];
        int row = 0;

        // Help taken from: https://www.ryadel.com/en/dobble-spot-it-algorithm-math-function-javascript/
        // Generate series from imageArr[0] to imageArr[numImages - 1]
        for (int i = 0; i <= numImages - 1; i++)  {
            cards[row][0] = imageArr[0];
            for (int i2 = 1; i2 <= numImages - 1; i2++) {
                cards[row][i2] = imageArr[(numImages * i) - i + i2];
            }
            row++;
        }

        // Generate series from imageArr[numImages] to imageArr[numImages * (numImages - 1)]
        for (int i = 1; i <= numImages-1; i++) {
            for (int i2 = 1; i2 <= numImages-1; i2++) {
                cards[row][0] = imageArr[i];
                for (int i3 = 1; i3 <= numImages-1; i3++) {
                    cards[row][i3] = imageArr[numImages + (numImages - 1) * (i3 - 1)
                            + ( (i - 1) * (i3 - 1) + (i2 - 1) )
                            % (numImages - 1)];
                }
                row++;
            }
        }

        shuffleCardsAndImages();
    }

    public void shuffleCardsAndImages(){
        for(int i = 0; i < numCardsTotal; i++){
            int rand = (int) ((Math.random() * (numCardsTotal - i)) + i);
            int[] tempCard = cards[i];
            cards[i] = cards[rand];
            cards[rand] = tempCard;

            for (int j = 0; j < numImages; j++) {
                rand = (int) ((Math.random() * (numImages - j)) + j);
                int tempImage = cards[i][j];
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
            if(cards[cardIndex - 1][i] == cards[cardIndex][imageIndex]){
                return true;
            }
        }
        return false;
    }
}
