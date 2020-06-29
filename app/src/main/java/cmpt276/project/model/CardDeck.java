package cmpt276.project.model;

/**
 * Deck of Cards
 * int[][]
 *      [] -> refers to card
 *      [][] -> refers to images on card
 */
public class CardDeck {

    private int numCards;
    private int numImages;
    private int[][] cards;

    private static CardDeck instance;
    private CardDeck() {}
    public static CardDeck getInstance(){
        if(instance == null){
            instance = new CardDeck();
        }
        return instance;
    }

    public void setNumCards(int numCards) {
        this.numCards = numCards;
    }

    public void setNumImages(int numImages) {
        this.numImages = numImages;
    }

    public void populateCards(){
        cards = new int[numCards][numImages];
        int row = 0;

        // Help taken from: https://www.ryadel.com/en/dobble-spot-it-algorithm-math-function-javascript/
        // Generate series from #01 to #N
        for (int i = 0; i <= numImages - 1; i++)  {
            cards[row][0] = 1;
            for (int i2 = 1; i2 <= numImages - 1; i2++) {
                cards[row][i2] = (numImages - 1) + (numImages - 1) * (i - 1) + (i2 + 1);
            }
            row++;
        }

        // Generate series from #N+1 to #N+(N-1)*(N-1)
        for (int i = 1; i <= numImages-1; i++) {
            for (int i2 = 1; i2 <= numImages-1; i2++) {
                cards[row][0] = i+1;
                for (int i3 = 1; i3 <= numImages-1; i3++) {
                    cards[row][i3] = (numImages + 1) + (numImages - 1) * (i3 - 1)
                            + ( ((i - 1) * (i3 - 1) + (i2 - 1)) )
                            % (numImages - 1);
                }
                row++;
            }
        }
    }

    public void print(){
        for (int r = 0; r < numCards; r++){
            for (int c = 0; c < numImages; c++){
                System.out.println("cards[" + r + "][" + c + "] " + cards[r][c]);
            }
        }
    }
}
