package eh.workout.journal.com.workoutjournal.db.relations;


import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;

public class ExerciseSetRepRelation {
    @Embedded
    private JournalSetEntity journalSetEntity;

    @Relation(parentColumn = "id", entityColumn = "journalSetId", entity = JournalRepEntity.class)
    private List<JournalRepEntity> journalRepEntityList;

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
}
