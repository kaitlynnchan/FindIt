package cmpt276.project.model;

import java.util.ArrayList;

/**
 * HIGH SCORES CLASS
 * Contains array of scores
 */
public class HighScores {
    private ArrayList<Score> scoreArray = new ArrayList<>();
    private int numMaxScores;

    private static HighScores instance;
    private HighScores() {}
    public static HighScores getInstance(){
        if(instance == null){
            instance = new HighScores();
        }
        return instance;
    }

    public int getNumMaxScores(){
        return scoreArray.size();
    }

    public ArrayList<Score> getScoreArray(){
        return scoreArray;
    }

    public Score getLastScore(){
        return scoreArray.get(numMaxScores - 1);
    }

    public void setNumMaxScores(int numMaxScores) {
        this.numMaxScores = numMaxScores;
    }

    public void setScoreArray(ArrayList<Score> sArray){
        this.scoreArray = sArray;
    }

    public void addScore(Score score) {
        if(scoreArray.size() < numMaxScores){
            int index = sort(score, scoreArray.size());
            scoreArray.add(index, score);
        } else{
            if(scoreArray.get(numMaxScores - 1).getTimeBySeconds() > score.getTimeBySeconds()){
                scoreArray.remove(numMaxScores - 1);
                int index = sort(score, numMaxScores);
                scoreArray.add(index, score);
            }
        }
    }

    private int sort(Score score, int size) {
        int i = 0;
        while (i < size) {
            if (scoreArray.get(i).getTimeBySeconds() > score.getTimeBySeconds()) {
                return i;
            } else {
                i++;
            }
        }
        return i;
    }

    public void resetScoreArray(){
        if (scoreArray.size() > 0) {
            scoreArray.subList(0, scoreArray.size()).clear();
        }
    }
}
