package cmpt276.project.model;

/**
 * SCORE CLASS
 * Object that contains time (s), name, and date
 */
public class Score {
    private int timeBySeconds;
    private String name;
    private String date;        // format: Month DD, YYY

    public Score(int timeBySeconds, String name, String date){
        this.timeBySeconds = timeBySeconds;
        this.name = name;
        this.date = date;
    }

    public int getTimeBySeconds() {
        return timeBySeconds;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "ScoreRecording{" +
                "timeBySeconds=" + timeBySeconds +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
