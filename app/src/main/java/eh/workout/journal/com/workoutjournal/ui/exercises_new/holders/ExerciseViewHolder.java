package eh.workout.journal.com.workoutjournal.ui.exercises_new.holders;

import android.annotation.SuppressLint;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerExerciseHolderBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.Exercise;

public class ExerciseViewHolder extends RecyclerView.ViewHolder {
    private RecyclerExerciseHolderBinding binding;
    private Exercise exercise;

    public ExerciseViewHolder(RecyclerExerciseHolderBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.binding.setHolder(this);
    }

    public void bind(Exercise exercise) {
        this.exercise = exercise;
        binding.setExercise(exercise);
    }


    public void onExerciseClicked(View view) {
//        if (listener != null) {
//            listener.exerciseSelected(exerciseLiftEntity);
//        }
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
//        if (exerciseLiftEntity.getIsShownInRecent()) {
//            popup.getMenu().findItem(R.id.action_move).setTitle("Remove Recent");
//            popup.getMenu().findItem(R.id.action_move).setVisible(true);
//        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
//                if (id == R.id.action_edit) {
//                    if (listener != null) {
//
//                    }
//                } else if (id == R.id.action_delete) {
//                    if (listener != null) {
////                            listener.removeFromRecent(exerciseLiftEntity);
//                    }
//                } else if (id == R.id.action_move) {
//                    if (listener != null) {
//                        listener.removeFromRecent(exerciseLiftEntity);
//                    }
//                }
                return true;
            }
        });
//            MenuPopupHelper optionsMenu = new MenuPopupHelper(view.getContext(), (MenuBuilder) popup.getMenu(), view, false, R.attr.actionOverflowMenuStyle, 0);
//            optionsMenu.setForceShowIcon(true);
//            optionsMenu.show();
        popup.show();
    }
}
