package eh.workout.journal.com.workoutjournal.model;

import com.echo.holographlibrary.Line;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;


public class OrmHistory {
    private Line line;
    private Line dateLine;
    private JournalRepEntity max;
    private double min;

    public OrmHistory(Line line, Line dateLine, JournalRepEntity max, double min) {
        this.line = line;
        this.dateLine = dateLine;
        this.max = max;
        this.min = min;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Line getDateLine() {
        return dateLine;
    }

    public void setDateLine(Line dateLine) {
        this.dateLine = dateLine;
    }

    public JournalRepEntity getMax() {
        return max;
    }

    public void setMax(JournalRepEntity max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }
}
