package eh.workout.journal.com.workoutjournal.ui.exercises;

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
import eh.workout.journal.com.workoutjournal.databinding.RecyclerPlanExerciseViewBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.PlanSetRelation;
import eh.workout.journal.com.workoutjournal.db.relations.RoutineSetRelation;

public class ExercisePlanRecyclerAdapter extends RecyclerView.Adapter<ExercisePlanRecyclerAdapter.PlanViewHolder> {
    List<PlanSetRelation> itemList = new ArrayList<>();
    ExercisePlanInterface listener;

    public interface ExercisePlanInterface {
        void onDeletePlan(PlanEntity planEntity);

        void onPlanClicked(PlanSetRelation planSetRelation);
    }

    public ExercisePlanRecyclerAdapter(ExercisePlanInterface listener) {
        this.listener = listener;
    }

    public void setItems(final List<PlanSetRelation> items) {
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
                PlanSetRelation newItem = items.get(newItemPosition);
                PlanSetRelation old = itemList.get(oldItemPosition);
                return old.getPlanEntity().getId().equals(newItem.getPlanEntity().getId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                PlanSetRelation old = itemList.get(oldItemPosition);
                PlanSetRelation newItem = items.get(newItemPosition);
                if (old.getPlanSetEntityList().size() != newItem.getPlanSetEntityList().size()) {
                    return false;
                }
                for (int i = 0; i < old.getPlanSetEntityList().size(); i++) {
                    if (!old.getPlanSetEntityList().get(i).getId().equals(newItem.getPlanSetEntityList().get(i).getId())) {
                        return false;
                    }
                }
                return old.getPlanEntity().getId().equals(newItem.getPlanEntity().getId())
                        && old.getPlanSetEntityList().size() == newItem.getPlanSetEntityList().size();
            }
        });
        this.itemList.clear();
        this.itemList.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlanViewHolder(RecyclerPlanExerciseViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
        RecyclerPlanExerciseViewBinding binding;
        PlanSetRelation planSetRelation;

        public PlanViewHolder(RecyclerPlanExerciseViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            binding.setListener(menuListener);
            planSetRelation = itemList.get(getAdapterPosition());
            String planList = null;
            for (int i = 0; i < planSetRelation.getPlanSetEntityList().size(); i++) {
                PlanSetEntity setEntity = planSetRelation.getPlanSetEntityList().get(i);
                if (planList == null) {
                    planList = "• " + setEntity.getName();
                } else {
                    planList = planList + "\n" + "• " + setEntity.getName();
                }
            }
            binding.planTitle.setText(planSetRelation.getPlanEntity().getPlanName());
            binding.planList.setText(planList);
            binding.cardPlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onPlanClicked(planSetRelation);
                    }
                }
            });
        }

        public View.OnClickListener menuListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.END);
                popup.getMenuInflater().inflate(R.menu.menu_edit_move_delete, popup.getMenu());
                popup.getMenu().findItem(R.id.action_edit).setVisible(false);
                popup.getMenu().findItem(R.id.action_move).setVisible(false);
                popup.getMenu().findItem(R.id.action_delete).setVisible(true);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.action_delete) {
                            listener.onDeletePlan(planSetRelation.getPlanEntity());
                        }
                        return true;
                    }
                });
                popup.show();
            }
        };
    }

}
