package eh.workout.journal.com.workoutjournal.util.loaders;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import eh.workout.journal.com.workoutjournal.db.entinty.Exercise;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseCategory;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;
import eh.workout.journal.com.workoutjournal.model.ExerciseCategories;
import eh.workout.journal.com.workoutjournal.model.Exercises;
import eh.workout.journal.com.workoutjournal.util.DataHelper;

public final class ExerciseLoaders {
    private static ExerciseLoaders instance;
    private static String exerciseCategoryFile = "exercise_category.json";
    private static String exerciseFile = "exercises.json";

    public static ExerciseLoaders get() {
        if (instance == null) {
            instance = new ExerciseLoaders();
        }
        return instance;
    }

    private ExerciseLoaders() {
    }


    public List<ExerciseLiftEntity> getExerciseLifts(Context context) {
        try {
            InputStream is = context.getAssets().open(exerciseFile);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            Exercises response = new Gson().fromJson(inputStreamReader, Exercises.class);
            HashMap<String, Integer> groupyList = new DataHelper().getCategoryId();
            List<ExerciseLiftEntity> exerciseLiftEntities = new ArrayList<>();
            for (Exercise exercise : response.getExercises()) {
                ExerciseLiftEntity exerciseLiftEntity = new ExerciseLiftEntity();
                exerciseLiftEntity.setId(UUID.randomUUID().toString());
                exerciseLiftEntity.setName(exercise.getName());
                exerciseLiftEntity.setExerciseEquipmentId(0);
                exerciseLiftEntity.setExerciseGroupId(groupyList.get(exercise.getCategory()));
                exerciseLiftEntity.setExerciseInputType(exercise.getExerciseEquipmentId());
                exerciseLiftEntity.setRecent(false);
                exerciseLiftEntities.add(exerciseLiftEntity);
            }
            return exerciseLiftEntities;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public List<ExerciseCategory> getExerciseCategories(Context context) {
//        try {
//            InputStream is = context.getAssets().open(exerciseCategoryFile);
//            InputStreamReader inputStreamReader = new InputStreamReader(is);
//            ExerciseCategories response = new Gson().fromJson(inputStreamReader, ExerciseCategories.class);
//            return response.getExerciseCategory();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public List<Exercise> getExercises(Context context) {
//        try {
//            InputStream is = context.getAssets().open(exerciseFile);
//            InputStreamReader inputStreamReader = new InputStreamReader(is);
//            Exercises response = new Gson().fromJson(inputStreamReader, Exercises.class);
//            HashMap<String, String> categoryList = new DataHelper().getCategoryList();
//            for (Exercise exercise : response.getExercises()) {
//                exercise.setRecent(false);
//                exercise.setCategoryName(categoryList.get(exercise.getCategory()));
//                if (exercise.getEquipmentString().contains("7")) {
//                    exercise.setExerciseEquipmentId(1);
//                }
//            }
//            return response.getExercises();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
