package eh.workout.journal.com.workoutjournal.db.relations;


import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;

public class ExerciseSetRepRelation {
    @Embedded
    public JournalSetEntity journalSetEntity;

    @Relation(parentColumn = "id", entityColumn = "journalSetId", entity = JournalRepEntity.class)
    private List<JournalRepEntity> journalRepEntityList;

    @Relation(parentColumn = "exerciseId", entityColumn = "exerciseId", entity = ExerciseOrmEntity.class)
    private List<ExerciseOrmEntity> exerciseOrmEntity;

    public JournalSetEntity getJournalSetEntity() {
        return journalSetEntity;
    }

    public void setJournalSetEntity(JournalSetEntity journalSetEntity) {
        this.journalSetEntity = journalSetEntity;
    }

    public List<JournalRepEntity> getJournalRepEntityList() {
        return journalRepEntityList;
    }

    public void setJournalRepEntityList(List<JournalRepEntity> journalRepEntityList) {
        this.journalRepEntityList = journalRepEntityList;
    }

    public List<ExerciseOrmEntity> getExerciseOrmEntity() {
        return exerciseOrmEntity;
    }

    public void setExerciseOrmEntity(List<ExerciseOrmEntity> exerciseOrmEntity) {
        this.exerciseOrmEntity = exerciseOrmEntity;
    }

    @Ignore
    @Override
    public String toString() {
        return journalSetEntity.getExerciseId();
    }
}
