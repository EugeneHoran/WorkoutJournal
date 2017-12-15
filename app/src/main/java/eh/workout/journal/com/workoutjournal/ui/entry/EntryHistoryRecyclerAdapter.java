package eh.workout.journal.com.workoutjournal.ui.entry;


import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.databinding.RecyclerRepItemBinding;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerRepNoWeightBinding;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerSetItemWithRecyclerBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.util.EquationsHelper;

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
        private EntryHistoryChildRepRecyclerAdapter adapter;
        private JournalSetEntity setEntity;

        EntryHistoryViewHolder(RecyclerSetItemWithRecyclerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            adapter = new EntryHistoryChildRepRecyclerAdapter();
        }

        void bindView() {
            ExerciseSetRepRelation dateSetRepRelation = itemList.get(getAdapterPosition());
            ExerciseOrmEntity ormEntity = dateSetRepRelation.getExerciseOrmEntity().get(0);
            setEntity = dateSetRepRelation.getJournalSetEntity();
            String title = String.valueOf(DateUtils.getRelativeTimeSpanString(setEntity.getTimestamp()));
            binding.setTitle(title);
            adapter.setOneRepMax(ormEntity.getOneRepMax(), ormEntity.getRepId());
            binding.recycler.setAdapter(adapter);
            adapter.setItems(dateSetRepRelation.getJournalRepEntityList());
            binding.recycler.setLayoutFrozen(true);
            binding.menuMore.setVisibility(View.GONE);
        }
    }

    /**
     * Child Adapter
     * <p>
     * Reps
     */
    class EntryHistoryChildRepRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int HOLDER_TYPE_WEIGHT = 0;
        private static final int HOLDER_TYPE_BODY = 1;
        private List<JournalRepEntity> itemList = new ArrayList<>();
        private int setOneRepMax;
        private String repId;

        void setOneRepMax(double oneRepMax, String repId) {
            setOneRepMax = EquationsHelper.getOneRepMaxInt(oneRepMax);
            this.repId = repId;
        }

        void setItems(List<JournalRepEntity> itemList) {
            this.itemList.clear();
            this.itemList.addAll(itemList);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            JournalRepEntity repEntity = itemList.get(position);
            if (repEntity.getExerciseInputType() == HOLDER_TYPE_WEIGHT) {
                return HOLDER_TYPE_WEIGHT;
            } else if (repEntity.getExerciseInputType() == HOLDER_TYPE_BODY) {
                return HOLDER_TYPE_BODY;
            } else {
                return super.getItemViewType(position);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HOLDER_TYPE_WEIGHT) {
                return new JournalRepWeightViewHolder(RecyclerRepItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            } else if (viewType == HOLDER_TYPE_BODY) {
                return new JournalRepViewHolder(RecyclerRepNoWeightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            } else {
                return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (viewHolder instanceof JournalRepWeightViewHolder) {
                JournalRepWeightViewHolder holder = (JournalRepWeightViewHolder) viewHolder;
                holder.bindView();
            } else if (viewHolder instanceof JournalRepViewHolder) {
                JournalRepViewHolder holder = (JournalRepViewHolder) viewHolder;
                holder.bindView();
            }
            viewHolder.itemView.setTag(this);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        class JournalRepViewHolder extends RecyclerView.ViewHolder {
            private RecyclerRepNoWeightBinding binding;

            JournalRepViewHolder(RecyclerRepNoWeightBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            void bindView() {
                JournalRepEntity repEntity = itemList.get(getAdapterPosition());
                binding.imageTrophy.setVisibility(repId.equals(repEntity.getId()) ? View.VISIBLE : View.INVISIBLE);
                binding.setSetPos(repEntity.getPosition());
                binding.setRepEntity(repEntity);
                int max = setOneRepMax;
                int progress = repEntity.getOrmInt();
                binding.progressBar.setMax(max);
                binding.progressBar.setProgress(progress);
            }
        }

        class JournalRepWeightViewHolder extends RecyclerView.ViewHolder {
            private RecyclerRepItemBinding binding;

            JournalRepWeightViewHolder(RecyclerRepItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            void bindView() {
                JournalRepEntity repEntity = itemList.get(getAdapterPosition());
                binding.imageTrophy.setVisibility(repId.equals(repEntity.getId()) ? View.VISIBLE : View.INVISIBLE);
                binding.setSetPos(repEntity.getPosition());
                binding.setRepEntity(repEntity);
                int max = setOneRepMax;
                int progress = repEntity.getOrmInt();
                binding.progressBar.setMax(max);
                binding.progressBar.setProgress(progress);
            }
        }
    }
}
