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
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.ui.shared.RepChildRecyclerAdapter;
import eh.workout.journal.com.workoutjournal.util.DataHelper;
import eh.workout.journal.com.workoutjournal.util.OrmHelper;
import eh.workout.journal.com.workoutjournal.util.diff.JournalChildDiffUtil;

public class JournalChildRecyclerAdapter extends RecyclerView.Adapter {
    private ArrayList<ExerciseSetRepRelation> setList = new ArrayList<>();
    private JournalRecyclerCallbacks listener;

    interface JournalRecyclerCallbacks {
        void onSetClicked(String setId, int inputType);

        void onDeleteSetClicked(ExerciseSetRepRelation dateSetRepRelation);
    }

    JournalChildRecyclerAdapter(JournalRecyclerCallbacks listener) {
        this.listener = listener;
    }

    void setItems(final List<ExerciseSetRepRelation> setItems) {
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new JournalChildDiffUtil(setList, setItems));
        this.setList.clear();
        this.setList.addAll(setItems);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new JournalViewHolder(RecyclerSetItemWithRecyclerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((JournalViewHolder) viewHolder).bindView(setList.get(position));
    }

    @Override
    public int getItemCount() {
        return setList.size();
    }

    public class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerSetItemWithRecyclerBinding binding;
        private RepChildRecyclerAdapter adapter;
        private ExerciseSetRepRelation setRepRelation;

        JournalViewHolder(RecyclerSetItemWithRecyclerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.adapter = new RepChildRecyclerAdapter(itemView.getContext());
            this.binding.recycler.setAdapter(adapter);
            this.binding.cardHolder.setOnClickListener(this);
            this.binding.imgMore.setOnClickListener(this);
        }

        void bindView(ExerciseSetRepRelation setRepRelation) {
            this.setRepRelation = setRepRelation;
            binding.setTitle(setRepRelation.getJournalSetEntity().getNameWithEquipment());
            adapter.setOneRepMax(OrmHelper.getOneRepMaxInt(setRepRelation.getExerciseOrmEntity().get(0).getOneRepMax()));
            adapter.setItems(setRepRelation.getJournalRepEntityList());
            binding.recycler.setLayoutFrozen(true);
        }

        @Override
        public void onClick(View view) {
            if (view == binding.cardHolder) {
                if (listener != null) {
                    listener.onSetClicked(
                            setRepRelation.getJournalSetEntity().getExerciseId(),
                            setRepRelation.getJournalSetEntity().getExerciseInputType());
                }
            } else if (view == binding.imgMore) {
                PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.END, R.attr.actionOverflowMenuStyle, 0);
                popup.getMenuInflater().inflate(R.menu.menu_edit_move_delete, popup.getMenu());
                popup.getMenu().findItem(R.id.action_delete).setVisible(true);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_delete) {
                            if (listener != null) {
                                listener.onDeleteSetClicked(setRepRelation);
                            }
                        }
                        return true;
                    }
                });
                popup.show();
            }
        }
    }
}
