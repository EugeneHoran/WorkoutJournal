package eh.workout.journal.com.workoutjournal.model;

public class DaySelector {
    private String dayName;
    private boolean selected = false;

    public DaySelector(String dayName, boolean selected) {
        this.dayName = dayName;
        this.selected = selected;
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
}
