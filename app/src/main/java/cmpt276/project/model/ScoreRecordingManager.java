package cmpt276.project.model;

import java.util.ArrayList;
import java.util.Iterator;

public class ScoreRecordingManager implements Iterable<ScoreRecording> {
    public ArrayList<ScoreRecording> ScoreArray = new ArrayList<>();
    private int size;


    public void add(ScoreRecording s) {
        ScoreArray.add(s);
        size++;
    }

    public void resetHighScore(){
        Iterator<ScoreRecording> iterator = ScoreArray.iterator();
        while(iterator.hasNext()){
            ScoreRecording s = iterator.next();
            iterator.remove();
            size--;
        }
        ScoreArray = new ArrayList<>();
        size = 0;
    }

    @Override
    public Iterator<ScoreRecording> iterator() {
        return ScoreArray.iterator();
    }

    public int getSize() {
        return size;
    }
}
