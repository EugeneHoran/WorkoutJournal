package eh.workout.journal.com.workoutjournal.util.loaders;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.Exercise;
import eh.workout.journal.com.workoutjournal.model.Exercises;

public final class ExerciseLoaders {
    private static ExerciseLoaders instance;

    public static String peopleFile = "exercises.json";

    public static ExerciseLoaders get() {
        if (instance == null) {
            instance = new ExerciseLoaders();
        }
        return instance;
    }

    private ExerciseLoaders() {
    }

    public List<Exercise> getExercises(Context context) {
        try {
            InputStream is = context.getAssets().open(peopleFile);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            Exercises response = new Gson().fromJson(inputStreamReader, Exercises.class);
            return response.getExercises();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
