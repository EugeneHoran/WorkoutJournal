package eh.workout.journal.com.workoutjournal.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseGroupEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.model.DaySelector;

public class DataHelper {


    public static final int EXERCISE_TYPE_WEIGHT = 0;
    public static final int EXERCISE_TYPE_BODY = 1;
    public static final int EXERCISE_TYPE_CARDIO = 2;
    public static final String[] DAYS = new String[]{
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
    };

    private static final String[] DAYS_SHORT = new String[]{
            "Sun",
            "Mon",
            "Tues",
            "Wed",
            "Thurs",
            "Fri",
            "Sat"
    };

    public static String getDaysShort(String daysInt) {
        StringBuilder daysFormatted = new StringBuilder();
        String[] exercise = daysInt.split(",");
        for (int i = 0; i < exercise.length; i++) {
            if (i != exercise.length - 1) {
                daysFormatted.append(DAYS_SHORT[Integer.valueOf(exercise[i]) - 1]).append(", ");
            } else {
                daysFormatted.append(DAYS_SHORT[Integer.valueOf(exercise[i]) - 1]);
            }
        }
        return daysFormatted.toString();
    }

    public HashMap<String, Integer> getCategoryId() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("11", 0);// Chest
        map.put("8", 1); // Arms
        map.put("13", 2);// Shoulders
        map.put("12", 3);// Back
        map.put("9", 4); // Legs
        map.put("14", 5); // Calves
        map.put("10", 6); // Abs
        return map;
    }

    private static final String[] EXERCISE_BODY_TYPE_ARRAY_NEW = new String[]{
            "Chest,0",
            "Arms,1",
            "Shoulders,2",
            "Back,3",
            "Legs,4",
            "Calves,5",
            "Abs,6",
            "Cardio,7",
            "Other,9"};

    private static final String[] EXERCISE_BODY_TYPE_ARRAY = new String[]{
            "Chest",
            "Arms",
            "Shoulders",
            "Back",
            "Legs",
            "Calves",
            "Abs",
            "Cardio",
            "Other"};

    public List<DaySelector> getDays() {
        List<DaySelector> daySelectorList = new ArrayList<>();
        for (int i = 0; i < DAYS.length; i++) {
            daySelectorList.add(new DaySelector(DAYS[i], false, i + 1));
        }
        return daySelectorList;
    }

    public List<ExerciseGroupEntity> generateExerciseGroups() {
        List<ExerciseGroupEntity> groupEntityList = new ArrayList<>();
        for (String typeArray : EXERCISE_BODY_TYPE_ARRAY_NEW) {
            String[] exercise = typeArray.split(",");
            int id = Integer.valueOf(exercise[1].trim());
            String name = exercise[0];
            groupEntityList.add(new ExerciseGroupEntity(id, name));
        }
        return groupEntityList;
    }


    public List<Object> getFormattedExerciseList(List<ExerciseLiftEntity> liftList) {
        List<Object> objectList = new ArrayList<>();
        List<ExerciseLiftEntity> recentList = new ArrayList<>();
        for (int i = 0; i < liftList.size(); i++) {
            ExerciseLiftEntity lift = liftList.get(i);
            if (lift != null) {
                if (lift.getRecent()) {
                    recentList.add(new ExerciseLiftEntity(lift));
                }
            }
        }
        if (recentList.size() != 0) {
            objectList.add("Recent Exercises");
            Collections.sort(recentList);
            Collections.reverse(recentList);
            objectList.addAll(recentList.subList(0, recentList.size() > 5 ? 5 : recentList.size()));
        }
        objectList.add("All Exercises");
        objectList.addAll(liftList);
        return objectList;
    }
}
