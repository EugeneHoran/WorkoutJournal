package eh.workout.journal.com.workoutjournal.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
    public static final String[] EXERCISE_TYPES = new String[]{
            "Weights or Machine",
            "Body Weight",
            "Cardio"
    };

    private static final String[] EXERCISE_BODY_PART = new String[]{
            "Chest",
            "Triceps",
            "Biceps",
            "Forearm",
            "Shoulders",
            "Back",
            "Legs",
            "Abs",
            "Cardio",
            "Other"};


    private static final String[] EXERCISE_BODY_TYPE_ARRAY = new String[]{
            "Chest,0",
            "Triceps,1",
            "Biceps,2",
            "Forearm,3",
            "Shoulders,4",
            "Back,5",
            "Legs,6",
            "Abs,7",
            "Cardio,8",
            "Other,9"};

    private static final String[] EXERCISE_LIFT_ARRAY = new String[]{
            "Bench Press,0,0",
            "Bench Press Incline,0,0",
            "Bench Press Decline,0,0",
            "Push Ups,0,1",
            "Bench Press (Dumbbell),0,0",
            "Bench Press Incline (Dumbbell),0,0",
            "Bench Press Decline (Dumbbell),0,0",
            "Hang Clean,4,0",
            "Power Clean,4,0",
            "Squat,6,0"};

    public List<DaySelector> getDays() {
        List<DaySelector> daySelectorList = new ArrayList<>();
        for (int i = 0; i < DAYS.length; i++) {
            daySelectorList.add(new DaySelector(DAYS[i], false, i + 1));
        }
        return daySelectorList;
    }

    public List<ExerciseGroupEntity> generateExerciseGroups() {
        List<ExerciseGroupEntity> groupEntityList = new ArrayList<>();
        for (String typeArray : EXERCISE_BODY_TYPE_ARRAY) {
            String[] exercise = typeArray.split(",");
            int id = Integer.valueOf(exercise[1].trim());
            String name = exercise[0];
            groupEntityList.add(new ExerciseGroupEntity(id, name));
        }
        return groupEntityList;
    }

    public static List<ExerciseLiftEntity> generateExerciseLifts() {
        List<ExerciseLiftEntity> exerciseLiftEntityList = new ArrayList<>();
        for (String liftArray : EXERCISE_LIFT_ARRAY) {
            String[] exercise = liftArray.split(",");
            String name = exercise[0];
            int groupId = Integer.valueOf(exercise[1]);
            int exerciseTypeId = Integer.valueOf(exercise[2]);

            ExerciseLiftEntity exerciseLiftEntity = new ExerciseLiftEntity();
            exerciseLiftEntity.setId(UUID.randomUUID().toString());
            exerciseLiftEntity.setName(name);
            exerciseLiftEntity.setExerciseGroupId(groupId);
            exerciseLiftEntity.setExerciseInputType(exerciseTypeId);
            exerciseLiftEntity.setRecent(false);
            exerciseLiftEntityList.add(exerciseLiftEntity);
        }
        return exerciseLiftEntityList;
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
