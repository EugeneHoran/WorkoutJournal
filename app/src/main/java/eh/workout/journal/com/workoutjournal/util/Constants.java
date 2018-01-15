package eh.workout.journal.com.workoutjournal.util;


public class Constants {


    /**
     * Settings
     */
    // Measure of units
    public static final String KEY_UNIT_MEASURES = "key_unit_measure";
    public static String SETTINGS_UNIT_MEASURE = "lbs";
    // Timer visibility
    public static final String KEY_ENABLED_TIMER = "key_enable_timer";
    public static boolean SETTINGS_SHOW_TIMER = false;
    //Routine plan
    public static final String KEY_ROUTINE_PLAN = "key_enable_routine_plan";
    public static boolean SETTINGS_SHOW_ROUTINE_PLAN = true;
    // Suggestions
    public static final String KEY_SHOW_SUGGESTIONS = "switch_suggestion";
    public static boolean SETTINGS_SHOW_SUGGESTIONS = true;

    /**
     * Exercise Input Type
     * Used for how users will input their exercise
     * <p>
     * weight and reps
     * reps
     * cardio
     */
    public static final int EXERCISE_TYPE_WEIGHT_REPS = 0;
    public static final int EXERCISE_TYPE_REPS = 1;
    public static final int EXERCISE_TYPE_CARDIO = 2;

    //
    public static final int ORM_ONE_REP_MAX = 0;
    public static final int ORM_PERCENTAGES = 1;


    public static final int JOURNAL_TOTAL_PAGES_DATES = 10000;
    public static final int JOURNAL_PAGE_TODAY = 5000;


    public static final String JOURNAL_PAGE_RESULT_CODE_SETTINGS = "page_result_code";
    public static final int REQUEST_CODE_SETTINGS = 50;


    public static final String JOURNAL_PAGE_RESULT_CODE_PLAN = "page_result_code";
    public static final int REQUEST_CODE_PLAN = 60;

    public static final String TOOLBAR_EXERCISE_TITLE = "Select Exercise";

    public static final String EDIT_PLAN_ID = "edit_plan_id";
    public static final String ADD_EDIT_PLAN_FROM_WHERE = "edit_plan_from_where";
    public static final int ADD_EDIT_PLAN_JOURNAL = 1;
    public static final int ADD_EDIT_PLAN_EXERCISE = 2;
}
