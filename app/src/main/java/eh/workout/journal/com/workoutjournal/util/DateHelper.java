package eh.workout.journal.com.workoutjournal.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import hirondelle.date4j.DateTime;

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

    public static DateTime getDateTime(long timestamp) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return new DateTime(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0, 0);
    }

    public static String getWorkoutStartDay(long timestamp) {
        Date date = new Date(timestamp);
        Long currentDayStart = getStartOfDay(date).getTime();
        return String.valueOf(currentDayStart);
    }

    public static DateTime getDateTimeFromString(String strTimestamp) {
        return getDateTime(Long.valueOf(strTimestamp));
    }

    /**
     * Get Start and End Times for day
     */

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

    /**
     * Timestamps
     */
    public static Long getTimestamp(Date date) {
        return date.getTime();
    }

    public static Date getDate(Long timestamp) {
        return new Date(timestamp);
    }

    public static Long[] getStartAndEndTimestamp(Date date) {
        Long[] timeStamps = new Long[2];
        Long currentDayStart = getStartOfDay(date).getTime();
        Long currentDayEnd = getEndOfDay(date).getTime();
        timeStamps[0] = currentDayStart;
        timeStamps[1] = currentDayEnd;
        return timeStamps;
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

    /**
     * Pager Adapter
     */
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

    public static Date getAdapterDate(int dayPosition) {
        int dayDiff = dayPosition - 5000;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Calendar mCalendar = new GregorianCalendar(year, month, day);
        mCalendar.add(Calendar.DATE, dayDiff);
        return mCalendar.getTime();
    }


    /**
     * OLD
     */


    public static Date toDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e1) {
            e1.printStackTrace();
            date = null;
        }
        return date;
    }

    public static Date toDate(String date) {
        Date newDate;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
        try {
            newDate = sdf.parse(date);
        } catch (ParseException e1) {
            e1.printStackTrace();
            newDate = null;
        }
        return newDate;
    }

    public static Date getTodaysDateFormatted() {
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e1) {
            e1.printStackTrace();
            date = null;
        }
        return date;
    }

}
