package eh.workout.journal.com.workoutjournal.ui.journal;


import android.support.v4.content.ContextCompat;
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
import eh.workout.journal.com.workoutjournal.db.entinty.PlanDayEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanDaySetEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.RoutineSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.PlanDaySetRelation;
import eh.workout.journal.com.workoutjournal.db.relations.RoutineSetRelation;
import eh.workout.journal.com.workoutjournal.util.views.LayoutUtil;

public class JournalRoutinePlanRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> itemList = new ArrayList<>();

    private PlanChildInterface listener;

    public interface PlanChildInterface {
        void onExerciseClicked(String setId, int inputType);

        void onEditRoutineClicked(String planId);

        void onDeleteRoutine(PlanDayEntity planDayEntity);

        void onEditPlanClicked(String planId);
    }

    JournalRoutinePlanRecyclerAdapter(PlanChildInterface listener) {
        this.listener = listener;
    }

    void setItems(List<Object> itemList) {
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
        int which = 0;
        private RecyclerPlanParentItemBinding binding;
        private JournalPlanChildRecyclerAdapter adapter;
        private RoutineSetRelation routineSetRelation;
        private PlanDaySetRelation planDaySetRelation;

        PlanParentViewHolder(RecyclerPlanParentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.setListener(menuListener);
            adapter = new JournalPlanChildRecyclerAdapter();
        }

        void bindItem() {
            which = itemList.get(getAdapterPosition()) instanceof RoutineSetRelation ? 0 : 1;
            binding.recycler.setNestedScrollingEnabled(false);
            if (which == 0) {
                binding.planTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_assignment, 0, 0, 0);
                routineSetRelation = (RoutineSetRelation) itemList.get(getAdapterPosition());
                binding.planTitle.setText(routineSetRelation.getRoutineEntity().getRoutineName());
                binding.recycler.setAdapter(adapter);
                adapter.setItems(new ArrayList<Object>(routineSetRelation.getPlanSetEntityList()));
            } else {
                binding.planTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_list, 0, 0, 0);
                planDaySetRelation = (PlanDaySetRelation) itemList.get(getAdapterPosition());
                binding.planTitle.setText(planDaySetRelation.getPlanDayEntity().getPlanName());
                binding.recycler.setAdapter(adapter);
                adapter.setItems(new ArrayList<Object>(planDaySetRelation.getPlanDaySetEntityList()));
            }
        }

        public View.OnClickListener menuListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.END);
                popup.getMenuInflater().inflate(R.menu.menu_edit_move_delete, popup.getMenu());
                popup.getMenu().findItem(R.id.action_move).setVisible(false);
                if (which == 0) {
                    popup.getMenu().findItem(R.id.action_edit).setTitle("Edit Routine");
                } else {
                    popup.getMenu().findItem(R.id.action_edit).setTitle("Edit Plan");
                    popup.getMenu().findItem(R.id.action_delete).setTitle("Delete Plan");
                }
                popup.getMenu().findItem(R.id.action_edit).setVisible(true);
                popup.getMenu().findItem(R.id.action_delete).setVisible(which != 0);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.action_edit) {
                            if (listener != null) {
                                if (which == 0) {
                                    listener.onEditRoutineClicked(routineSetRelation.getRoutineEntity().getId());
                                } else {
                                    listener.onEditPlanClicked(planDaySetRelation.getPlanDayEntity().getId());
                                }
                            }
                        } else if (id == R.id.action_delete) {
                            if (listener != null) {
                                listener.onDeleteRoutine(planDaySetRelation.getPlanDayEntity());
                            }
                        }
                        return true;
                    }
                });
                popup.show();
            }
        };
    }

    private class JournalPlanChildRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Object> itemList = new ArrayList<>();

        void setItems(List<Object> itemList) {
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
            private PlanDaySetEntity planDaySetEntity;

            PlanChildViewHolder(RecyclerPlanChildItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                itemView.setOnClickListener(this);
            }

            void bindItem() {
                if (itemList.get(getAdapterPosition()) instanceof RoutineSetEntity) {
                    planSetEntity = (RoutineSetEntity) itemList.get(getAdapterPosition());
                    binding.setName(planSetEntity.getName());
                    if (planSetEntity.isSetCompleted()) {
                        binding.imageEdit.setImageDrawable(new LayoutUtil().getDrawableMutate(itemView.getContext(), R.drawable.ic_check_circle, R.color.colorAccent));
                    } else {
                        binding.imageEdit.setImageResource(R.drawable.ic_chevron_right);
                    }
                } else {
                    planDaySetEntity = (PlanDaySetEntity) itemList.get(getAdapterPosition());
                    binding.setName(planDaySetEntity.getName());
                    if (planDaySetEntity.isSetCompleted()) {
                        binding.imageEdit.setImageResource(R.drawable.ic_check_circle);
                        binding.imageEdit.setImageDrawable(new LayoutUtil().getDrawableMutate(itemView.getContext(), R.drawable.ic_check_circle, R.color.colorAccent));
                    } else {
                        binding.imageEdit.setImageResource(R.drawable.ic_chevron_right);
                    }
                }
            }

            @Override
            public void onClick(View view) {
                if (itemList.get(getAdapterPosition()) instanceof RoutineSetEntity) {
                    listener.onExerciseClicked(planSetEntity.getExerciseId(), planSetEntity.getExerciseInputType());
                } else {
                    listener.onExerciseClicked(planDaySetEntity.getExerciseId(), planDaySetEntity.getExerciseInputType());
                }
            }
        }
    }
}