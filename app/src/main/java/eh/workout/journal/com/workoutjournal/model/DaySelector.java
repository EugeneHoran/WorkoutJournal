package eh.workout.journal.com.workoutjournal.model;

public class DaySelector {
    private String dayName;
    private boolean selected = false;
    private int dayInt;

    public DaySelector(String dayName, boolean selected, int dayInt) {
        this.dayName = dayName;
        this.selected = selected;
        this.dayInt = dayInt;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getDayInt() {
        return dayInt;
    }

    public void setDayInt(int dayInt) {
        this.dayInt = dayInt;
    }
}
