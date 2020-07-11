package cmpt276.project.model;

import java.util.ArrayList;

//use array to store scores
public class ScoreRecordingManager{
    private ArrayList<ScoreRecording> scoreArray = new ArrayList<>();
    private final int size = 5;
    private void selectionSort() {
        int index;
        for (int i = 0; i < size; i++){
            index = i;
            for(int j = i + 1; j < size; j++){
                if(scoreArray.get(i).getTimeBySeconds() > scoreArray.get(j).getTimeBySeconds()){
                    index = j;
                }
                if(index != i){
                    ScoreRecording temp = scoreArray.get(i);
                    scoreArray.remove(i);
                    scoreArray.set(i, scoreArray.get(j));
                    scoreArray.remove(j);
                    scoreArray.set(j, temp);
                }
            }
        }
    }

    public void addNewScore(ScoreRecording s) {
        if(scoreArray.size() < size){
            scoreArray.add(s);
            selectionSort();
        }
        else{
            selectionSort();
            scoreArray.remove(4);
            scoreArray.set(4, s);
            selectionSort();
        }
    }

    //to reset scores
    public void resetHighScore(){
        scoreArray = new ArrayList<>();
    }


    public void setScoreArray(ArrayList<ScoreRecording> sArray){
        this.scoreArray = sArray;
    }

    public ArrayList<ScoreRecording> getScoreArray(){
        return scoreArray;
    }
}
