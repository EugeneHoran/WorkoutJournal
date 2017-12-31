package eh.workout.journal.com.workoutjournal.ui.journal;


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
import eh.workout.journal.com.workoutjournal.databinding.RecyclerPlanChildItemBinding;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerPlanParentItemBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.RoutineSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.RoutineSetRelation;
import eh.workout.journal.com.workoutjournal.util.views.LayoutUtil;

public class JournalPlanRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RoutineSetRelation> itemList = new ArrayList<>();

    private PlanChildInterface listener;

    public interface PlanChildInterface {
        void onExerciseClicked(String setId, int inputType);

        void onEditPlanClicked(String planId);
    }

    JournalPlanRecyclerAdapter(PlanChildInterface listener) {
        this.listener = listener;
    }

    void setItems(List<RoutineSetRelation> itemList) {
        if (itemList == null) {
            this.itemList.clear();
            notifyDataSetChanged();
            return;
        }
        this.itemList.clear();
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlanParentViewHolder(RecyclerPlanParentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        PlanParentViewHolder holderPlan = (PlanParentViewHolder) viewHolder;
        holderPlan.bindItem();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class PlanParentViewHolder extends RecyclerView.ViewHolder {
        private RecyclerPlanParentItemBinding binding;
        private JournalPlanChildRecyclerAdapter adapter;
        private RoutineSetRelation routineSetRelation;

        PlanParentViewHolder(RecyclerPlanParentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.setHolder(this);
            adapter = new JournalPlanChildRecyclerAdapter();
        }

        void bindItem() {
            routineSetRelation = itemList.get(getAdapterPosition());
            binding.planTitle.setText(routineSetRelation.getRoutineEntity().getRoutineName());
            binding.recycler.setAdapter(adapter);
            adapter.setItems(routineSetRelation.getPlanSetEntityList());
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
                        if (listener != null) {
                            listener.onEditPlanClicked(routineSetRelation.getRoutineEntity().getId());
                        }
                    }
                    return true;
                }
            });
            popup.show();
        }
    }

    private class JournalPlanChildRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<RoutineSetEntity> itemList = new ArrayList<>();


        void setItems(List<RoutineSetEntity> itemList) {
            this.itemList.clear();
            this.itemList.addAll(itemList);
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PlanChildViewHolder(RecyclerPlanChildItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            PlanChildViewHolder holder = (PlanChildViewHolder) viewHolder;
            holder.bindItem();
        }

        @Override
        public int getItemCount() {
            return itemList == null ? 0 : itemList.size();
        }

        class PlanChildViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private RecyclerPlanChildItemBinding binding;
            private RoutineSetEntity planSetEntity;

            PlanChildViewHolder(RecyclerPlanChildItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                itemView.setOnClickListener(this);
            }

            void bindItem() {
                planSetEntity = itemList.get(getAdapterPosition());
                binding.setSetEntity(planSetEntity);
                if (planSetEntity.isSetCompleted()) {
                    binding.imageEdit.setImageResource(R.drawable.ic_edit);
                    binding.imageComplete.setImageDrawable(LayoutUtil.getDrawableMutate(itemView.getContext(), R.drawable.ic_check_circle, R.color.colorAccent));
                } else {
                    binding.imageEdit.setImageResource(R.drawable.ic_chevron_right);
                    binding.imageComplete.setImageDrawable(null);
                }
            }

            @Override
            public void onClick(View view) {
                listener.onExerciseClicked(planSetEntity.getExerciseId(), planSetEntity.getExerciseInputType());
            }
        }
    }
}