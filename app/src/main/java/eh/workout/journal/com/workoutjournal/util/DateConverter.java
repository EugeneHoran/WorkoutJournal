package eh.workout.journal.com.workoutjournal.util;


import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    private static DateFormat df = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT, Locale.getDefault());

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        if (value != null) {
            try {
                return df.parse(String.valueOf(value));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }
}
