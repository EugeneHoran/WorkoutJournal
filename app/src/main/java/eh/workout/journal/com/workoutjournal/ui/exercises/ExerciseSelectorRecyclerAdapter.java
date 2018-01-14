package eh.workout.journal.com.workoutjournal.ui.exercises;


import android.annotation.SuppressLint;
import android.support.v7.util.DiffUtil;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
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
            if (getAdapterPosition() == itemList.size() - 1) {
                binding.divider.setVisibility(View.GONE);
            } else {
                if (getAdapterPosition() < itemList.size() - 1) {
                    if (itemList.get(getAdapterPosition() + 1) instanceof String) {
                        binding.divider.setVisibility(View.GONE);
                    } else {
                        binding.divider.setVisibility(View.VISIBLE);
                    }
                }
            }
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

        @SuppressLint("RestrictedApi")
        public void onMenuMoreClicked(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.END, R.attr.actionOverflowMenuStyle, 0);
            popup.getMenuInflater().inflate(R.menu.menu_edit_move_delete, popup.getMenu());
            popup.getMenu().findItem(R.id.action_edit).setTitle("Edit Exercise");
            popup.getMenu().findItem(R.id.action_delete).setTitle("Delete Exercise");
            popup.getMenu().findItem(R.id.action_edit).setVisible(true);
            popup.getMenu().findItem(R.id.action_info).setVisible(true);
            popup.getMenu().findItem(R.id.action_delete).setVisible(true);
            if (exerciseLiftEntity.getIsShownInRecent()) {
                popup.getMenu().findItem(R.id.action_move).setTitle("Remove Recent");
                popup.getMenu().findItem(R.id.action_move).setVisible(true);
            }
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.action_edit) {
                        if (listener != null) {

                        }
                    } else if (id == R.id.action_delete) {
                        if (listener != null) {
//                            listener.removeFromRecent(exerciseLiftEntity);
                        }
                    } else if (id == R.id.action_move) {
                        if (listener != null) {
                            listener.removeFromRecent(exerciseLiftEntity);
                        }
                    }
                    return true;
                }
            });
//            MenuPopupHelper optionsMenu = new MenuPopupHelper(view.getContext(), (MenuBuilder) popup.getMenu(), view, false, R.attr.actionOverflowMenuStyle, 0);
//            optionsMenu.setForceShowIcon(true);
//            optionsMenu.show();
            popup.show();
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
