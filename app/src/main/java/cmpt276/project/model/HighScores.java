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
        return numMaxScores;
    }

    public ArrayList<Score> getScoreArray(){
        return scoreArray;
    }

    public Score getBestScore(){
        return scoreArray.get(0);
    }

    public void setNumMaxScores(int numMaxScores) {
        this.numMaxScores = numMaxScores;
    }

    public void setScoreArray(ArrayList<Score> sArray){
        this.scoreArray = sArray;
    }

    public void addScore(Score score) {
        int index = sort(score);
        if(index != -1){
            scoreArray.add(index, score);
        }
    }

    // returns -1 if the score is already in the list
    private int sort(Score score) {
        int i = 0;
        while (i < scoreArray.size()) {
            if(scoreArray.get(i).getTimeBySeconds() > score.getTimeBySeconds()){
                return i;
            } else if(scoreArray.get(i).getTimeBySeconds() == score.getTimeBySeconds()){
                return -1;
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

    public void print(){
        for(int i = 0; i < scoreArray.size(); i++){
            System.out.println(scoreArray.get(i).toString());
        }
    }
}
