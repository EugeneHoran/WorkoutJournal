package eh.workout.journal.com.workoutjournal.ui.journal;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerPlanChildItemBinding;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerPlanParentItemBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.PlanSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.PlanSetRelation;
import eh.workout.journal.com.workoutjournal.util.LayoutUtil;

public class JournalPlanRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PlanSetRelation> itemList = new ArrayList<>();

    private PlanChildInterface listener;

    public interface PlanChildInterface {
        void onExerciseClicked(String setId, int inputType);
    }

    JournalPlanRecyclerAdapter(PlanChildInterface listener) {
        this.listener = listener;
    }

    void setItems(List<PlanSetRelation> itemList) {
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
        PlanParentViewHolder holder = (PlanParentViewHolder) viewHolder;
        holder.bindItem();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    class PlanParentViewHolder extends RecyclerView.ViewHolder {
        RecyclerPlanParentItemBinding binding;
        JournalPlanChildRecyclerAdapter adapter;

        PlanParentViewHolder(RecyclerPlanParentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            adapter = new JournalPlanChildRecyclerAdapter();
        }

        void bindItem() {
            PlanSetRelation planSetRelation = itemList.get(getAdapterPosition());
            binding.recycler.setAdapter(adapter);
            adapter.setItems(planSetRelation.getPlanSetEntityList());
        }
    }

    private class JournalPlanChildRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<PlanSetEntity> itemList = new ArrayList<>();


        void setItems(List<PlanSetEntity> itemList) {
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
            private PlanSetEntity planSetEntity;

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
                    binding.imageComplete.setImageDrawable(LayoutUtil.getDrawableMutate(itemView.getContext(), R.drawable.ic_circle_empty, R.color.black));
                }
            }

            @Override
            public void onClick(View view) {
                listener.onExerciseClicked(planSetEntity.getExerciseId(), planSetEntity.getExerciseInputType());
            }
        }
    }
}