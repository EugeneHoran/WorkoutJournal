package eh.workout.journal.com.workoutjournal.ui.entry;


import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.databinding.RecyclerSetItemWithRecyclerBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.ui.shared.RepChildRecyclerAdapter;

public class EntryHistoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ExerciseSetRepRelation> itemList = new ArrayList<>();

    void setItems(final List<ExerciseSetRepRelation> items) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return itemList.size();
            }

            @Override
            public int getNewListSize() {
                return items.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                ExerciseSetRepRelation old = itemList.get(oldItemPosition);
                ExerciseSetRepRelation newItem = items.get(newItemPosition);
                return old.getJournalSetEntity().getId().equals(newItem.getJournalSetEntity().getId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                ExerciseSetRepRelation old = itemList.get(oldItemPosition);
                ExerciseSetRepRelation newItem = items.get(newItemPosition);
                return old.getJournalSetEntity().getId().equals(newItem.getJournalSetEntity().getId())
                        && old.getJournalRepEntityList().size() == newItem.getJournalRepEntityList().size();
            }
        });
        this.itemList.clear();
        this.itemList.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EntryHistoryViewHolder(RecyclerSetItemWithRecyclerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        EntryHistoryViewHolder holder = (EntryHistoryViewHolder) viewHolder;
        holder.bindView();
        viewHolder.itemView.setTag(this);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class EntryHistoryViewHolder extends RecyclerView.ViewHolder {
        private RecyclerSetItemWithRecyclerBinding binding;
        private RepChildRecyclerAdapter adapter;
        private JournalSetEntity setEntity;

        EntryHistoryViewHolder(RecyclerSetItemWithRecyclerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            adapter = new RepChildRecyclerAdapter();
        }

        void bindView() {
            ExerciseSetRepRelation dateSetRepRelation = itemList.get(getAdapterPosition());
            ExerciseOrmEntity ormEntity = dateSetRepRelation.getExerciseOrmEntity().get(0);
            setEntity = dateSetRepRelation.getJournalSetEntity();
            String title = String.valueOf(DateUtils.getRelativeTimeSpanString(setEntity.getTimestamp()));
            binding.setTitle(title);
            adapter.setOneRepMax(ormEntity);
            binding.recycler.setAdapter(adapter);
            if (dateSetRepRelation.getJournalRepEntityList() != null) {
                if (dateSetRepRelation.getJournalRepEntityList().size() > 0) {
                    adapter.setItems(dateSetRepRelation.getJournalRepEntityList());
                }
            }
            binding.recycler.setLayoutFrozen(true);
            binding.menuMore.setVisibility(View.GONE);
        }
    }
}
