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
        if (values == null) {
            return null;
        }
        StringBuilder item = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            item.append(values.get(i));
        }
        return item.toString();
    }
}
