package eh.workout.journal.com.workoutjournal.ui.exercises;


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
import eh.workout.journal.com.workoutjournal.db.entinty.PlanSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.PlanSetRelation;

public class ExercisePlanRecyclerAdapter extends RecyclerView.Adapter<ExercisePlanRecyclerAdapter.PlanViewHolder> {
    List<PlanSetRelation> itemList = new ArrayList<>();
    ExercisePlanInterface listener;

    public interface ExercisePlanInterface {
        void onPlanClicked(String planId);

        void onEditPlanClicked(String planId);
    }

    public ExercisePlanRecyclerAdapter(ExercisePlanInterface listener) {
        this.listener = listener;
    }

    public void setItems(List<PlanSetRelation> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
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
            binding.setHolder(this);
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
        }

        public void onMenuClicked(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.END);
            popup.getMenuInflater().inflate(R.menu.menu_edit_move_delete, popup.getMenu());
            popup.getMenu().findItem(R.id.action_move).setVisible(false);
            popup.getMenu().findItem(R.id.action_delete).setVisible(false);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.action_edit) {
                        listener.onEditPlanClicked(planSetRelation.getPlanEntity().getId());
                    }
                    return true;
                }
            });
            popup.show();
        }
    }

}
