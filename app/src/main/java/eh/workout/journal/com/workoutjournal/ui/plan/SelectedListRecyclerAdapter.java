package eh.workout.journal.com.workoutjournal.ui.plan;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.databinding.RecyclerExercisesSelectedBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;

public class SelectedListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    SelectListCallback listener;

    public interface SelectListCallback {
        void onRemoveItem(ExerciseLiftEntity exerciseLiftEntity);
    }

    public void setListener(SelectListCallback listener) {
        this.listener = listener;
    }

    private List<ExerciseLiftEntity> itemList = new ArrayList<>();

    public void setItems(final List<ExerciseLiftEntity> items) {
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
                ExerciseLiftEntity newItem = items.get(newItemPosition);
                ExerciseLiftEntity old = itemList.get(oldItemPosition);
                return old.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                ExerciseLiftEntity old = itemList.get(oldItemPosition);
                ExerciseLiftEntity newItem = items.get(newItemPosition);
                return old.getName().equals(newItem.getName());
            }
        });
        this.itemList.clear();
        this.itemList.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderTest(RecyclerExercisesSelectedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolderTest) {
            ViewHolderTest holder = (ViewHolderTest) viewHolder;
            holder.bindView();
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolderTest extends RecyclerView.ViewHolder {
        RecyclerExercisesSelectedBinding binding;

        ViewHolderTest(RecyclerExercisesSelectedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bindView() {
            final ExerciseLiftEntity exerciseLiftEntity = itemList.get(getAdapterPosition());
            binding.setObject(exerciseLiftEntity);
            binding.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onRemoveItem(exerciseLiftEntity);
//                        itemList.remove(getAdapterPosition());
//                        notifyItemChanged(getAdapterPosition());
                    }
                }
            });
        }
    }
}
