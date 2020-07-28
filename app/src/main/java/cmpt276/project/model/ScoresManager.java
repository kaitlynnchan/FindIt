package cmpt276.project.model;

import java.util.ArrayList;

/**
 * SCORES MANAGER CLASS
 * Stores an array list of scores
 */
public class ScoresManager {
    private ArrayList<Score> scoreArray = new ArrayList<>();
    private int numMaxScores;

    public int getNumMaxScores(){
        return numMaxScores;
    }

    public ArrayList<Score> getScoreArray(){
        return scoreArray;
    }

    public Score getScore(int index){
        return scoreArray.get(index);
    }

    public void setNumMaxScores(int numMaxScores) {
        this.numMaxScores = numMaxScores;
    }

    public void setScoreArray(ArrayList<Score> sArray){
        this.scoreArray = sArray;
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
