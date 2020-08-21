package project.findit.model;

import java.util.ArrayList;

/**
 * SCORES MANAGER CLASS
 * Creates and stores an arraylist of scores
 */
public class ScoresManager {

    private ArrayList<Score> scoreArray = new ArrayList<>();
    private int numMaxScores;

    public ArrayList<Score> getScoreArray(){
        return scoreArray;
    }

    public void setScoreArray(ArrayList<Score> sArray){
        this.scoreArray = sArray;
    }

    public int getNumMaxScores(){
        return numMaxScores;
    }

    public void setNumMaxScores(int numMaxScores) {
        this.numMaxScores = numMaxScores;
    }

    public Score getScore(int index){
        return scoreArray.get(index);
    }

    public void addScore(Score score) {
        int index = sort(score);
        scoreArray.add(index, score);
    }

    private int sort(Score score) {
        int i = 0;
        while (i < scoreArray.size()) {
            if(score.getTimeBySeconds() < scoreArray.get(i).getTimeBySeconds()){
                return i;
            } else{
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
