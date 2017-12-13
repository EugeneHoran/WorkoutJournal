package eh.workout.journal.com.workoutjournal.model;


public interface JournalRep {
    String getId();

    long getTimestamp();

    int getPosition();

    String getLiftName();

    String getReps();

    String getWeight();

    String getJournalSetId();

    double getOneRepMax();
}
