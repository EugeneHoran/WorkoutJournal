package eh.workout.journal.com.workoutjournal.ui.exercises;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.databinding.RecyclerExerciseItemGroupBinding;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerGroupHeaderItemBinding;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerGroupItemBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseGroupEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;

public class ExerciseGroupRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_GROUP_ITEM = 0;
    private static final int TYPE_EXERCISE_HEADER_ITEM = 1;
    private static final int TYPE_EXERCISE_ITEM = 2;

    private List<Object> objectList = new ArrayList<>();
    private GroupRecyclerInterface listener;

    ExerciseGroupRecyclerAdapter(GroupRecyclerInterface listener) {
        this.listener = listener;
    }

    interface GroupRecyclerInterface {
        void onGroupItemClicked(ExerciseGroupEntity exerciseGroupEntity);

        void onReturnToGroupClicked();

        void exerciseSelected(ExerciseLiftEntity exerciseLift);
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
            return new GroupViewHolder(RecyclerGroupItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_EXERCISE_HEADER_ITEM) {
            return new HeaderViewHolder(RecyclerGroupHeaderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_EXERCISE_ITEM) {
            return new ExerciseViewHolder(RecyclerExerciseItemGroupBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
        } else if (viewHolder instanceof ExerciseViewHolder) {
            ExerciseViewHolder holder = (ExerciseViewHolder) viewHolder;
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
        private RecyclerGroupItemBinding binding;
        private ExerciseGroupEntity groupEntity;

        GroupViewHolder(RecyclerGroupItemBinding binding) {
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

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private RecyclerExerciseItemGroupBinding binding;
        private ExerciseLiftEntity exerciseLiftEntity;

        ExerciseViewHolder(RecyclerExerciseItemGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bindView() {
            exerciseLiftEntity = (ExerciseLiftEntity) objectList.get(getAdapterPosition());
            binding.setExercise(exerciseLiftEntity);
            binding.setListener(exerciseClickListener);
        }

        View.OnClickListener exerciseClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.exerciseSelected(exerciseLiftEntity);
                }
            }
        };
    }

}
