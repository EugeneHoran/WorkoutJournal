package eh.workout.journal.com.workoutjournal.db.relations;


import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;

public class DateSetRepRelation {
    @Embedded
    public JournalSetEntity journalSetEntity;

    @Relation(parentColumn = "exerciseId", entityColumn = "exerciseId", entity = ExerciseOrmEntity.class)
    List<ExerciseOrmEntity> exerciseOrmEntity;

    @Relation(parentColumn = "id", entityColumn = "journalSetId", entity = JournalRepEntity.class)
    List<JournalRepEntity> journalRepEntityList;

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

    public void setExerciseOrmEntity(List<ExerciseOrmEntity> exerciseOrmEntity) {
        this.exerciseOrmEntity = exerciseOrmEntity;
    }

    public List<ExerciseOrmEntity> getExerciseOrmEntity() {
        return exerciseOrmEntity;
    }
}
