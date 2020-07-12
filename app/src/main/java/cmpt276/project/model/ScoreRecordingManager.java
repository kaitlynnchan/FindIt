package cmpt276.project.model;

import java.util.ArrayList;

//use array to store scores
public class ScoreRecordingManager{
    private ArrayList<ScoreRecording> scoreArray = new ArrayList<>();
    private int numScores;

    private static ScoreRecordingManager instance;
    private ScoreRecordingManager() {}
    public static ScoreRecordingManager getInstance(){
        if(instance == null){
            instance = new ScoreRecordingManager();
        }
        return instance;
    }

    public void setNumScores(int numScores) {
        this.numScores = numScores;
    }

    public void addNewScore(ScoreRecording s) {
        if(scoreArray.size() < numScores){
            sort(s, scoreArray.size());
        } else{
            if(scoreArray.get(numScores - 1).getTimeBySeconds() > s.getTimeBySeconds()){
                scoreArray.remove(numScores - 1);
                sort(s, numScores);
            }
        }
    }

    private void sort(ScoreRecording s, int size) {
        int i = 0;
        while (i < size) {
            if (scoreArray.get(i).getTimeBySeconds() > s.getTimeBySeconds()) {
                break;
            } else {
                i++;
            }
        }
        scoreArray.add(i, s);
    }

    public void print(){
        for(int i = 0; i < scoreArray.size(); i++){
            System.out.println(scoreArray.get(i).toString());
        }
    }

    public int getNumScores(){
        return scoreArray.size();
    }

    public void resetHighScore(){
        for(int i = scoreArray.size()-1; i >= 0; i--){
            scoreArray.remove(i);
        }
    }

    public void setScoreArray(ArrayList<ScoreRecording> sArray){
        this.scoreArray = sArray;
    }

    public ArrayList<ScoreRecording> getScoreArray(){
        return scoreArray;
    }

    public ScoreRecording getLastScore(){
        return scoreArray.get(numScores - 1);
    }
}
