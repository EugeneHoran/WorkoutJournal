package eh.workout.journal.com.workoutjournal.ui.exercises;


import android.support.v7.util.DiffUtil;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerRoutineExerciseBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.RoutineSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.RoutineSetRelation;
import eh.workout.journal.com.workoutjournal.util.DataHelper;

public class ExerciseRoutineRecyclerAdapter extends RecyclerView.Adapter<ExerciseRoutineRecyclerAdapter.PlanViewHolder> {
    private List<RoutineSetRelation> itemList = new ArrayList<>();
    private ExercisePlanInterface listener;

    public interface ExercisePlanInterface {
        void onEditPlanClicked(String planId);
    }

    ExerciseRoutineRecyclerAdapter(ExercisePlanInterface listener) {
        this.listener = listener;
    }

    public void setItems(final List<RoutineSetRelation> items) {
        this.itemList.clear();
        this.itemList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlanViewHolder(RecyclerRoutineExerciseBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(PlanViewHolder holder, int position) {
        holder.bindItem();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class PlanViewHolder extends RecyclerView.ViewHolder {
        RecyclerRoutineExerciseBinding binding;
        RoutineSetRelation routineSetRelation;

        PlanViewHolder(RecyclerRoutineExerciseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            binding.setListener(menuListener);
            routineSetRelation = itemList.get(getAdapterPosition());
            binding.routineDays.setText(DataHelper.getDaysShort(routineSetRelation.getRoutineEntity().getRoutineDayListString()));
            StringBuilder planList = null;
            for (int i = 0; i < routineSetRelation.getPlanSetEntityList().size(); i++) {
                RoutineSetEntity setEntity = routineSetRelation.getPlanSetEntityList().get(i);
                if (TextUtils.isEmpty(planList)) {
                    planList = new StringBuilder("• " + setEntity.getName());
                } else {
                    planList.append("\n" + "• ").append(setEntity.getName());
                }
            }
            binding.planTitle.setText(routineSetRelation.getRoutineEntity().getRoutineName());
            binding.planList.setText(planList);
            binding.cardPlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onEditPlanClicked(routineSetRelation.getRoutineEntity().getId());
                    }
                }
            });
        }

        public View.OnClickListener menuListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.END);
                popup.getMenuInflater().inflate(R.menu.menu_edit_move_delete, popup.getMenu());
                popup.getMenu().findItem(R.id.action_move).setVisible(false);
                popup.getMenu().findItem(R.id.action_delete).setVisible(false);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.action_edit) {
                            if (listener != null) {
                                listener.onEditPlanClicked(routineSetRelation.getRoutineEntity().getId());
                            }
                        }
                        return true;
                    }
                });
                popup.show();
            }
        };
    }

}
