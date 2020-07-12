package cmpt276.project.model;

import java.util.ArrayList;

/**
 * SCORE BOARD
 * Contains array of scores
 */
public class ScoreBoard {
    private ArrayList<Score> scoreArray = new ArrayList<>();
    private int numScores;

    private static ScoreBoard instance;
    private ScoreBoard() {}
    public static ScoreBoard getInstance(){
        if(instance == null){
            instance = new ScoreBoard();
        }
        return instance;
    }

    public int getNumScores(){
        return scoreArray.size();
    }

    public ArrayList<Score> getScoreArray(){
        return scoreArray;
    }

    public Score getLastScore(){
        return scoreArray.get(numScores - 1);
    }

    public void setNumScores(int numScores) {
        this.numScores = numScores;
    }

    public void setScoreArray(ArrayList<Score> sArray){
        this.scoreArray = sArray;
    }

    public void addScore(Score score) {
        if(scoreArray.size() < numScores){
            sort(score, scoreArray.size());
        } else{
            if(scoreArray.get(numScores - 1).getTimeBySeconds() > score.getTimeBySeconds()){
                scoreArray.remove(numScores - 1);
                sort(score, numScores);
            }
        }
    }

    private void sort(Score score, int size) {
        int i = 0;
        while (i < size) {
            if (scoreArray.get(i).getTimeBySeconds() > score.getTimeBySeconds()) {
                break;
            } else {
                i++;
            }
        }
        scoreArray.add(i, score);
    }

    public void resetHighScore(){
        if (scoreArray.size() > 0) {
            scoreArray.subList(0, scoreArray.size()).clear();
        }
    }

    public void print(){
        for(int i = 0; i < scoreArray.size(); i++){
            System.out.println(scoreArray.get(i).toString());
        }
    }
}
