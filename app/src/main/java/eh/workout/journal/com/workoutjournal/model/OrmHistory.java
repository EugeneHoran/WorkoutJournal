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


    @Override
    public String toString() {
        StringBuilder linePoints = null;
        if (line != null) {
            for (int i = 0; i < line.getPoints().size(); i++) {
                if (linePoints == null) {
                    linePoints = new StringBuilder(String.valueOf(line.getPoints().get(i).getX() + line.getPoints().get(i).getY()));
                } else {
                    linePoints.append(String.valueOf(line.getPoints().get(i).getX() + line.getPoints().get(i).getY()));
                }
            }
        }
        StringBuilder lineDate = null;
        if (dateLine != null) {
            for (int i = 0; i < dateLine.getPoints().size(); i++) {
                if (lineDate == null) {
                    lineDate = new StringBuilder(String.valueOf(dateLine.getPoints().get(i).getX() + dateLine.getPoints().get(i).getY()));
                } else {
                    lineDate.append(String.valueOf(dateLine.getPoints().get(i).getX() + dateLine.getPoints().get(i).getY()));
                }
            }
        }
        String maxAmount;
        if (max != null) {
            maxAmount = max.getOneRepMax() + "";
        } else {
            maxAmount = "null";
        }
        return "OrmHistory{" +
                "line=" + linePoints +
                ", dateLine=" + lineDate +
                ", max=" + maxAmount +
                ", min=" + String.valueOf(min) +
                '}';
    }

}
