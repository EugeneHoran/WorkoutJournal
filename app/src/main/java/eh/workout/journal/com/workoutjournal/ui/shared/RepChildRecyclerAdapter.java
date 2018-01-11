package eh.workout.journal.com.workoutjournal.ui.shared;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerHeaderRepAndWeightBinding;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerRepWeightBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.views.LayoutUtil;

public class RepChildRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_HEADER = 0;
    private static final int HOLDER_ITEM = 1;

    private ArrayList<JournalRepEntity> itemList = new ArrayList<>();
    private int setOneRepMax;
    private Drawable trophyGray, trophyBlue;

    public RepChildRecyclerAdapter(Context context) {
        LayoutUtil layoutUtil = new LayoutUtil();
        this.trophyGray = layoutUtil.getDrawableMutate(context, R.drawable.ic_trophy, R.color.colorGrayTransparent);
        this.trophyBlue = layoutUtil.getDrawableMutate(context, R.drawable.ic_trophy, R.color.colorAccent);
    }

    public void setOneRepMax(int setOneRepMax) {
        this.setOneRepMax = setOneRepMax;
    }

    public void setItems(List<JournalRepEntity> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HOLDER_HEADER;
        } else {
            return HOLDER_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HOLDER_HEADER) {
            return new HeaderViewHolder(RecyclerHeaderRepAndWeightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == HOLDER_ITEM) {
            return new WeightRepViewHolder(RecyclerRepWeightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case HOLDER_HEADER:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
                headerViewHolder.bindView(itemList.get(position).getExerciseInputType() == Constants.EXERCISE_TYPE_WEIGHT_REPS);
                break;
            case HOLDER_ITEM:
                WeightRepViewHolder weightRepViewHolder = (WeightRepViewHolder) viewHolder;
                weightRepViewHolder.bindView(itemList.get(position - 1).getExerciseInputType() == Constants.EXERCISE_TYPE_WEIGHT_REPS);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size() == 0 ? 0 : itemList.size() + 1;
    }

    /**
     * Headers
     */
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        RecyclerHeaderRepAndWeightBinding binding;

        HeaderViewHolder(RecyclerHeaderRepAndWeightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindView(boolean isWeightAndReps) {
            binding.weight.setVisibility(isWeightAndReps ? View.VISIBLE : View.GONE);
            binding.spacer1.setVisibility(isWeightAndReps ? View.GONE : View.VISIBLE);
            binding.spacer2.setVisibility(isWeightAndReps ? View.GONE : View.VISIBLE);
            binding.repMax.setText(String.format("%% of 1RM (%s %s)", setOneRepMax,
                    isWeightAndReps ? Constants.SETTINGS_UNIT_MEASURE : "reps"));
        }
    }

    class WeightRepViewHolder extends RecyclerView.ViewHolder {
        private RecyclerRepWeightBinding binding;
        private JournalRepEntity repEntity;

        WeightRepViewHolder(RecyclerRepWeightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindView(boolean isWeightAndReps) {
            repEntity = itemList.get(getAdapterPosition() - 1);
            binding.setSetPos(repEntity.getPosition());
            binding.setRepEntity(repEntity);
            binding.progressBar.setMax(setOneRepMax);
            binding.progressBar.setProgress(repEntity.getOrmInt());
            binding.imageTrophy.setImageDrawable(repEntity.getOrmInt() == setOneRepMax ? trophyBlue : trophyGray);
            binding.weight.setVisibility(isWeightAndReps ? View.VISIBLE : View.GONE);
            binding.spacer2.setVisibility(isWeightAndReps ? View.GONE : View.VISIBLE);
            binding.spacer1.setVisibility(isWeightAndReps ? View.GONE : View.VISIBLE);
            binding.weight.setVisibility(isWeightAndReps ? View.VISIBLE : View.GONE);
        }
    }
}