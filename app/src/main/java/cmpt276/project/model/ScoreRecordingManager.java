package cmpt276.project.model;

//use array to store scores
public class ScoreRecordingManager{
    private ScoreRecording[] scoreArray;


    public void addNewScore(ScoreRecording s) {
        ScoreRecording[] tempScoreArray = new ScoreRecording[scoreArray.length + 1];
        for(int i = 0; i < scoreArray.length; i++){
            tempScoreArray[i] = scoreArray[i];
        }
        tempScoreArray[scoreArray.length] = s;
        scoreArray = new ScoreRecording[tempScoreArray.length];
        for(int i = 0; i < tempScoreArray.length; i++){
            scoreArray[i] = tempScoreArray[i];
        }
    }


    //to reset scores
    public void resetHighScore(){
        scoreArray = new ScoreRecording[0];
    }

    //to sort scores
    public void selectionSort(){
        int index = 0;
        for (int i = 0; i < scoreArray.length; i++){
            index = i;
            for(int j = i + 1; j < scoreArray.length; j++){
                if(scoreArray[i].getTimeBySeconds() > scoreArray[j].getTimeBySeconds()){
                    index = j;
                }
                if(index != i){
                    ScoreRecording temp = scoreArray[i];
                    scoreArray[i] = scoreArray[index];
                    scoreArray[index] = temp;
                }
            }
        }
    }

    public ScoreRecording[] getScoreArray(){
        return scoreArray;
    }
}
