package eh.workout.journal.com.workoutjournal.ui.plan;


import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerExerciseItemGroupBinding;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerGroupHeaderItemBinding;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerGroupItemBinding;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerGroupItemRoutinePlanBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseGroupEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;

public class PlanGroupRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_GROUP_ITEM = 0;
    private static final int TYPE_EXERCISE_HEADER_ITEM = 1;
    private static final int TYPE_EXERCISE_ITEM = 2;

    private List<Object> objectList = new ArrayList<>();
    private GroupRecyclerInterface listener;

    PlanGroupRecyclerAdapter(GroupRecyclerInterface listener) {
        this.listener = listener;
    }

    interface GroupRecyclerInterface {
        void onGroupItemClicked(ExerciseGroupEntity exerciseGroupEntity);

        void onReturnToGroupClicked();

        void removeSelectedLift(ExerciseLiftEntity exerciseLiftEntity);

        void addSelectedLift(ExerciseLiftEntity exerciseLiftEntity);
    }

    public void setItems(final List<Object> items) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return objectList.size();
            }

            @Override
            public int getNewListSize() {
                return items.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                Object oldItem = objectList.get(oldItemPosition);
                Object newItem = items.get(newItemPosition);
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Object oldItem = objectList.get(oldItemPosition);
                Object newItem = items.get(newItemPosition);
                if (oldItem instanceof String && newItem instanceof String) {
                    String oldString = (String) oldItem;
                    String newString = (String) newItem;
                    return oldString.equalsIgnoreCase(newString);
                } else if (oldItem instanceof ExerciseGroupEntity && newItem instanceof ExerciseGroupEntity) {
                    ExerciseGroupEntity oldGroup = (ExerciseGroupEntity) oldItem;
                    ExerciseGroupEntity newGroup = (ExerciseGroupEntity) newItem;
                    return oldGroup.getId() == newGroup.getId();
                } else {
                    ExerciseLiftEntity oldExercise = (ExerciseLiftEntity) oldItem;
                    ExerciseLiftEntity newExercise = (ExerciseLiftEntity) newItem;
                    return oldExercise.getId().equals(newExercise.getId());
                }
            }
        });
        objectList = items;
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemViewType(int position) {
        Object object = objectList.get(position);
        if (object instanceof ExerciseGroupEntity) {
            return TYPE_GROUP_ITEM;
        } else if (object instanceof String) {
            return TYPE_EXERCISE_HEADER_ITEM;
        } else if (object instanceof ExerciseLiftEntity) {
            return TYPE_EXERCISE_ITEM;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_GROUP_ITEM) {
            return new GroupViewHolder(RecyclerGroupItemRoutinePlanBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_EXERCISE_HEADER_ITEM) {
            return new HeaderViewHolder(RecyclerGroupHeaderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_EXERCISE_ITEM) {
            return new ViewHolderLifts(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_add_plan_lift_item, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof GroupViewHolder) {
            GroupViewHolder holder = (GroupViewHolder) viewHolder;
            holder.bindView();
        } else if (viewHolder instanceof HeaderViewHolder) {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            holder.bindView();
        } else if (viewHolder instanceof ViewHolderLifts) {
            ViewHolderLifts holder = (ViewHolderLifts) viewHolder;
            holder.bindView();
        }
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private RecyclerGroupHeaderItemBinding binding;

        HeaderViewHolder(RecyclerGroupHeaderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bindView() {
            String title = (String) objectList.get(getAdapterPosition());
            binding.setName(title);
            binding.setListener(groupClickListener);
        }

        View.OnClickListener groupClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onReturnToGroupClicked();
                }
            }
        };
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        private RecyclerGroupItemRoutinePlanBinding binding;
        private ExerciseGroupEntity groupEntity;

        GroupViewHolder(RecyclerGroupItemRoutinePlanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bindView() {
            groupEntity = (ExerciseGroupEntity) objectList.get(getAdapterPosition());
            binding.setName(groupEntity.getName());
            binding.setListener(groupClickListener);
        }

        View.OnClickListener groupClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onGroupItemClicked(groupEntity);
                }
            }
        };
    }


    public class ViewHolderLifts extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        TextView liftName;
        CheckBox checkBox;
        private ExerciseLiftEntity exerciseLiftEntity;

        ViewHolderLifts(View itemView) {
            super(itemView);
            liftName = itemView.findViewById(R.id.liftName);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.liftParent).setOnClickListener(this);
        }

        private void bindView() {
            exerciseLiftEntity = (ExerciseLiftEntity) objectList.get(getAdapterPosition());
            liftName.setText(exerciseLiftEntity.getName());
            checkBox.setOnCheckedChangeListener(this);
            checkBox.setChecked(exerciseLiftEntity.isSelected());
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            for (int i = 1; i < objectList.size(); i++) {
                if (objectList.get(i) instanceof ExerciseLiftEntity) {
                    ExerciseLiftEntity exerciseLiftEntity1 = (ExerciseLiftEntity) objectList.get(i);
                    if (exerciseLiftEntity1.getId().equalsIgnoreCase(exerciseLiftEntity.getId())) {
                        exerciseLiftEntity1.setSelected(checkBox.isChecked());
                    }
                }
            }
            exerciseLiftEntity.setSelected(checkBox.isChecked());
            if (listener != null) {
                if (exerciseLiftEntity.isSelected()) {
                    listener.addSelectedLift(exerciseLiftEntity);
                } else {
                    listener.removeSelectedLift(exerciseLiftEntity);
                }
            }
        }

        @Override
        public void onClick(View view) {
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
            } else {
                checkBox.setChecked(true);
            }
        }
    }
}
