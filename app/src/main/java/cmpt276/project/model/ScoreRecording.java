package cmpt276.project.model;

public class ScoreRecording {
    private int timeBySeconds;
    private String name;
    private String month;
    private int day;
    private int year;

    public ScoreRecording(int timeBySeconds, String name, String month, int day, int year){
        this.timeBySeconds = timeBySeconds;
        this.name = name;
        this.month = month;
        this.day = day;
        this.year = year;
    }

    public int getTimeBySeconds() {
        return timeBySeconds;
    }

    public String getName() {
        return name;
    }

    public String getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }
}
