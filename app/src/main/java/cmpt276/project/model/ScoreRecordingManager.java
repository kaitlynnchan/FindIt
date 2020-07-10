package cmpt276.project.model;

//use array to store scores
public class ScoreRecordingManager{
    public ScoreRecording[] scoreArray;
    private int size;

    public ScoreRecordingManager(){
        scoreArray = new ScoreRecording[size];
        size = 0;
    }

    public ScoreRecordingManager(ScoreRecording[] sR, int sRSize){
        size = sRSize;
        for(int i = 0; i < size; i++){
            scoreArray[i] = sR[i];
        }
    }

    public void add(ScoreRecording s) {
        ScoreRecording[] tempScoreArray = new ScoreRecording[size + 1];
        for(int i = 0; i < size; i++){
            tempScoreArray[i] = scoreArray[i];
        }
        tempScoreArray[size] = s;
        size++;
        scoreArray = new ScoreRecording[size];
        for(int i = 0; i < size; i++){
            scoreArray[i] = tempScoreArray[i];
        }
    }

    //to reset scores
    public void resetHighScore(){
        scoreArray = new ScoreRecording[0];
        size = 0;
    }

    //to sort scores
    public void selectionSort(){
        int index = 0;
        for (int i = 0; i < size; i++){
            index = i;
            for(int j = i + 1; j < size; j++){
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


    public int getSize() {
        return size;
    }
}
