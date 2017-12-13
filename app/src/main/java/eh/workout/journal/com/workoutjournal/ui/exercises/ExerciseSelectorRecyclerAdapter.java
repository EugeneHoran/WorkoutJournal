package eh.workout.journal.com.workoutjournal.ui.exercises;


import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eh.workout.journal.com.workoutjournal.databinding.RecyclerExerciseItemBinding;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerHeaderBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;

public class ExerciseSelectorRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_HEADER = 0;
    private static final int HOLDER_EXERCISE = 1;

    private ExerciseAdapterInterface listener;

    void setListener(ExerciseAdapterInterface listener) {
        this.listener = listener;
    }

    interface ExerciseAdapterInterface {
        void exerciseSelected(ExerciseLiftEntity exerciseLift);

        void removeFromRecent(ExerciseLiftEntity exerciseLift);
    }

    private List<Object> itemList;

    void setItems(final List<Object> items) {
        if (itemList == null) {
            itemList = items;
            notifyDataSetChanged();
            return;
        }
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
                Object old = itemList.get(oldItemPosition);
                Object newItem = items.get(newItemPosition);
                if (old instanceof String) {
                    String oldString = (String) old;
                    if (newItem instanceof String) {
                        String newString = (String) newItem;
                        return oldString.equals(newString);
                    } else {
                        return false;
                    }
                } else {
                    ExerciseLiftEntity oldExercise = (ExerciseLiftEntity) old;
                    if (newItem instanceof ExerciseLiftEntity) {
                        ExerciseLiftEntity newExercise = (ExerciseLiftEntity) newItem;
                        return oldExercise.getId().equals(newExercise.getId());
                    } else {
                        return false;
                    }
                }
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Object old = itemList.get(oldItemPosition);
                Object newItem = items.get(newItemPosition);
                if (old instanceof String) {
                    String oldString = (String) old;
                    if (newItem instanceof String) {
                        String newString = (String) newItem;
                        return oldString.equals(newString);
                    } else {
                        return false;
                    }
                } else {
                    ExerciseLiftEntity oldExercise = (ExerciseLiftEntity) old;
                    if (newItem instanceof ExerciseLiftEntity) {
                        ExerciseLiftEntity newExercise = (ExerciseLiftEntity) newItem;
                        return oldExercise.getId().equals(newExercise.getId()) &&
                                oldExercise.getIsShownInRecent() == newExercise.getIsShownInRecent();
                    } else {
                        return false;
                    }
                }
            }
        });
        itemList = items;
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemViewType(int position) {
        Object object = itemList.get(position);
        if (object instanceof String) {
            return HOLDER_HEADER;
        } else {
            return HOLDER_EXERCISE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HOLDER_HEADER) {
            return new ViewHolderHeader(RecyclerHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ViewHolderExercise(RecyclerExerciseItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolderHeader) {
            ViewHolderHeader holder = (ViewHolderHeader) viewHolder;
            holder.bindView();
        } else if (viewHolder instanceof ViewHolderExercise) {
            ViewHolderExercise holder = (ViewHolderExercise) viewHolder;
            holder.bindView();
        }
        viewHolder.itemView.setTag(this);
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }


    public class ViewHolderExercise extends RecyclerView.ViewHolder {
        RecyclerExerciseItemBinding binding;
        ExerciseLiftEntity exerciseLiftEntity;

        ViewHolderExercise(RecyclerExerciseItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bindView() {
            exerciseLiftEntity = (ExerciseLiftEntity) itemList.get(getAdapterPosition());
            binding.setExercise(exerciseLiftEntity);
            binding.setHolder(this);
        }

        public void onExerciseClicked(View view) {
            if (listener != null) {
                listener.exerciseSelected(exerciseLiftEntity);
            }
        }

        public void onRemoveFromRecents(View view) {
            if (listener != null) {
                listener.removeFromRecent(exerciseLiftEntity);
            }
        }
    }

    class ViewHolderHeader extends RecyclerView.ViewHolder {
        RecyclerHeaderBinding binding;

        ViewHolderHeader(RecyclerHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bindView() {
            binding.setItemText((String) itemList.get(getAdapterPosition()));
        }
    }

}
