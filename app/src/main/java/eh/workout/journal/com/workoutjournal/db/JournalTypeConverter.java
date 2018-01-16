package eh.workout.journal.com.workoutjournal.db;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JournalTypeConverter {
    @TypeConverter
    public List<String> fromInteger(String value) {
        List<String> muscles = new ArrayList<>();
        if (value == null) {
            return muscles;
        }
        muscles.addAll(Arrays.asList(value.split(",")));
        return muscles;
    }

    @TypeConverter
    public String dateToTimestamp(List<String> values) {
        StringBuilder item = null;
        for (int i = 0; i < values.size(); i++) {
            item.append(values.get(i));
        }
        if (item == null) {
            return null;
        }
        return item.toString();
    }
}
