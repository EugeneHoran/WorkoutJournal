package eh.workout.journal.com.workoutjournal.util;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;

public class DataGenerator {
    private static final String[] EXERCISE_LIFT_ARRAY = new String[]{
            "Bench Press",
            "Bench Press Incline",
            "Bench Press Decline",
            "Hang Clean",
            "Power Clean",
            "Squat"};

    public static List<ExerciseLiftEntity> generateExerciseLifts() {
        List<ExerciseLiftEntity> exerciseLiftEntityList = new ArrayList<>();
        for (String aEXERCISE_LIFT_ARRAY : EXERCISE_LIFT_ARRAY) {
            ExerciseLiftEntity exerciseLiftEntity = new ExerciseLiftEntity();
            exerciseLiftEntity.setId(UUID.randomUUID().toString());
            exerciseLiftEntity.setName(aEXERCISE_LIFT_ARRAY);
            exerciseLiftEntity.setRecent(false);
            exerciseLiftEntityList.add(exerciseLiftEntity);
        }
        return exerciseLiftEntityList;
    }

    public static List<Object> getFormattedExerciseList(List<ExerciseLiftEntity> liftList) {
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
            objectList.addAll(recentList);
        }
        objectList.add("All Exercises");
        objectList.addAll(liftList);
        return objectList;
    }
}
