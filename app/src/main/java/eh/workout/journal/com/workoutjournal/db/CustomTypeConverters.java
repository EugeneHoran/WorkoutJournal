package eh.workout.journal.com.workoutjournal.db;


public class CustomTypeConverters {
    public static String fromDayListToString(Integer[] days) {
        if (days.length == 0) {
            return "";
        }
        String daysString = "";
        String adding;
        for (int i = 0; i < days.length; i++) {
            if (i == 0) {
                adding = String.valueOf(days[i]);
            } else {
                adding = daysString + "," + String.valueOf(days[i]);
            }
            daysString = adding;
        }
        return daysString;
    }

    public static Integer[] fromStringToDayList(String daysString) {
        if (daysString == null) {
            return new Integer[0];
        }
        String[] dayStringList = daysString.split(",");
        Integer[] dayIntList = new Integer[dayStringList.length];
        for (int i = 0; i < dayStringList.length; i++) {
            dayIntList[i] = Integer.valueOf(dayStringList[i]);
        }
        return dayIntList;
    }
}
