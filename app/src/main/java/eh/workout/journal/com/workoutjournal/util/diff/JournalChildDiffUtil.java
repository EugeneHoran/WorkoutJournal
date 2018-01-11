package eh.workout.journal.com.workoutjournal.util.diff;

import android.support.v7.util.DiffUtil;

import java.util.List;

import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;

public class JournalChildDiffUtil extends DiffUtil.Callback {
    private List<ExerciseSetRepRelation> oldList, newList;

    public JournalChildDiffUtil(List<ExerciseSetRepRelation> oldList, List<ExerciseSetRepRelation> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList == null ? 0 : oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList == null ? 0 : newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getJournalSetEntity().getId().equals(newList.get(newItemPosition).getJournalSetEntity().getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        ExerciseSetRepRelation old = oldList.get(oldItemPosition);
        ExerciseSetRepRelation newItem = newList.get(newItemPosition);
        if (old.getExerciseOrmEntity().get(0).getOneRepMax() != newItem.getExerciseOrmEntity().get(0).getOneRepMax() &&
                old.getExerciseOrmEntity().get(0).getReps().equals(newItem.getExerciseOrmEntity().get(0).getReps()) &&
                old.getExerciseOrmEntity().get(0).getWeight().equals(newItem.getExerciseOrmEntity().get(0).getWeight())) {
            return false;
        }
        if (old.getJournalRepEntityList().size() != newItem.getJournalRepEntityList().size()) {
            return false;
        }
        for (int i = 0; i < old.getJournalRepEntityList().size(); i++) {
            if (!old.getJournalRepEntityList().get(i).getId().equals(newItem.getJournalRepEntityList().get(i).getId())
                    && !old.getJournalRepEntityList().get(i).getWeight().equals(newItem.getJournalRepEntityList().get(i).getWeight())
                    && !old.getJournalRepEntityList().get(i).getReps().equals(newItem.getJournalRepEntityList().get(i).getReps())) {
                return false;
            }
        }
        return true;
    }
}