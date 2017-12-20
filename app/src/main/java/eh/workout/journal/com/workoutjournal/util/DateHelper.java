package eh.workout.journal.com.workoutjournal.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateHelper {

    public static int findDaysDiff(long unixStartTime, long unixEndTime) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(unixStartTime);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(unixEndTime);
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);
        return (int) ((calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / (24 * 60 * 60 * 1000));
    }

    public static int getDayOfWeek(long timestamp) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(timestamp);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        return calendar1.get(Calendar.DAY_OF_WEEK);
    }

    private static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Long[] getStartAndEndTimestamp(long timestamp) {
        Date date = new Date(timestamp);
        Long[] timeStamps = new Long[2];
        Long currentDayStart = getStartOfDay(date).getTime();
        Long currentDayEnd = getEndOfDay(date).getTime();
        timeStamps[0] = currentDayStart;
        timeStamps[1] = currentDayEnd;
        return timeStamps;
    }

    public static Long getAdapterTimestamp(int dayPosition) {
        int dayDiff = dayPosition - 5000;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Calendar mCalendar = new GregorianCalendar(year, month, day);
        mCalendar.add(Calendar.DATE, dayDiff);
        return mCalendar.getTime().getTime();
    }
}
