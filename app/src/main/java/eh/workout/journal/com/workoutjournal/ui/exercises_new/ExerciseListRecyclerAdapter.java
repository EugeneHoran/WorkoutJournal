package eh.workout.journal.com.workoutjournal.ui.exercises_new;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.databinding.RecyclerExerciseHolderBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.Exercise;
import eh.workout.journal.com.workoutjournal.ui.exercises_new.holders.ExerciseViewHolder;

public class ExerciseListRecyclerAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {
    private List<Exercise> itemList = new ArrayList<>();

    void setItems(List<Exercise> items) {
        itemList.clear();
        itemList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExerciseViewHolder(RecyclerExerciseHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
