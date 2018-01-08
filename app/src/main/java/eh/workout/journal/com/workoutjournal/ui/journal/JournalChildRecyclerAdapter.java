package eh.workout.journal.com.workoutjournal.ui.journal;


import android.support.v7.util.DiffUtil;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerSetItemWithRecyclerBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.ui.shared.RepChildRecyclerAdapter;

public class JournalChildRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ExerciseSetRepRelation> itemList = new ArrayList<>();
    private JournalRecyclerInterface listener;

    JournalChildRecyclerAdapter(JournalRecyclerInterface listener) {
        this.listener = listener;
    }

    interface JournalRecyclerInterface {
        void onWorkoutClicked(String setId, int inputType);

        void onDeleteSetClicked(ExerciseSetRepRelation dateSetRepRelation);
    }

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
                ExerciseSetRepRelation newItem = items.get(newItemPosition);
                ExerciseSetRepRelation old = itemList.get(oldItemPosition);
                return old.getJournalSetEntity().getId().equals(newItem.getJournalSetEntity().getId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                ExerciseSetRepRelation old = itemList.get(oldItemPosition);
                ExerciseSetRepRelation newItem = items.get(newItemPosition);
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
        });
        this.itemList.clear();
        this.itemList.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new JournalViewHolder(RecyclerSetItemWithRecyclerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        JournalViewHolder holder = (JournalViewHolder) viewHolder;
        holder.bindView();
        viewHolder.itemView.setTag(this);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class JournalViewHolder extends RecyclerView.ViewHolder {
        private RecyclerSetItemWithRecyclerBinding binding;
        private RepChildRecyclerAdapter adapter;
        private ExerciseSetRepRelation dateSetRepRelation;
        private JournalSetEntity setEntity;

        JournalViewHolder(RecyclerSetItemWithRecyclerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            adapter = new RepChildRecyclerAdapter();
        }

        void bindView() {
            dateSetRepRelation = itemList.get(getAdapterPosition());
            setEntity = dateSetRepRelation.getJournalSetEntity();
            if (dateSetRepRelation.getExerciseOrmEntity().size() > 0) {
                ExerciseOrmEntity ormEntity = dateSetRepRelation.getExerciseOrmEntity().get(0);
                adapter.setOneRepMax(ormEntity);
            }
            binding.setHideMenu(false);
            binding.setTitle(setEntity.getName());
            binding.setListener(workoutClickListener);
            binding.setMenuListener(menuClickListener);
            binding.recycler.setAdapter(adapter);
            adapter.setItems(dateSetRepRelation.getJournalRepEntityList());
            binding.recycler.setLayoutFrozen(true);
        }

        View.OnClickListener menuClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        };

        View.OnClickListener workoutClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onWorkoutClicked(setEntity.getExerciseId(), setEntity.getExerciseInputType());
                }
            }
        };

        private void showMenu(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.END);
            popup.getMenuInflater().inflate(R.menu.menu_edit_move_delete, popup.getMenu());
            popup.getMenu().findItem(R.id.action_edit).setVisible(false);
            popup.getMenu().findItem(R.id.action_move).setVisible(false);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.action_delete) {
                        if (listener != null) {
                            listener.onDeleteSetClicked(dateSetRepRelation);
                        }
                    }
                    return true;
                }
            });
            popup.show();
        }
    }
}
