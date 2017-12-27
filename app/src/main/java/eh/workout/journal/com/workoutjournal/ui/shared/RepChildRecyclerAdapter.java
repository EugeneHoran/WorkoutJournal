package eh.workout.journal.com.workoutjournal.ui.shared;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.databinding.RecyclerRepHeaderBinding;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerRepItemBinding;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerRepNoWeightBinding;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerRepWeightHeaderBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.OrmHelper;

public class RepChildRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_HEADER_REP_WEIGHT = 10;
    private static final int HOLDER_HEADER_REP = 11;
    private static final int HOLDER_TYPE_WEIGHT = 0;
    private static final int HOLDER_TYPE_BODY = 1;

    private List<JournalRepEntity> itemList = new ArrayList<>();
    private int setOneRepMax;
    private String repId;
    private ExerciseOrmEntity ormEntity;

    public void setOneRepMax(ExerciseOrmEntity ormEntity) {
        this.ormEntity = ormEntity;
        setOneRepMax = OrmHelper.getOneRepMaxInt(ormEntity.getOneRepMax());
        repId = ormEntity.getRepId();
    }

    public void setItems(List<JournalRepEntity> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (ormEntity != null) {
                if (ormEntity.getInputType() == Constants.EXERCISE_TYPE_WEIGHT) {
                    return HOLDER_HEADER_REP_WEIGHT;
                } else {
                    return HOLDER_HEADER_REP;
                }
            } else {
                return HOLDER_HEADER_REP_WEIGHT;
            }
        } else {
            JournalRepEntity repEntity = itemList.get(position - 1);
            if (repEntity.getExerciseInputType() == HOLDER_TYPE_WEIGHT) {
                return HOLDER_TYPE_WEIGHT;
            } else if (repEntity.getExerciseInputType() == HOLDER_TYPE_BODY) {
                return HOLDER_TYPE_BODY;
            } else {
                return super.getItemViewType(position);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HOLDER_HEADER_REP_WEIGHT) {
            return new RepWeightHeaderViewHolder(RecyclerRepWeightHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == HOLDER_HEADER_REP) {
            return new RepHeaderViewHolder(RecyclerRepHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == HOLDER_TYPE_WEIGHT) {
            return new JournalRepWeightViewHolder(RecyclerRepItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == HOLDER_TYPE_BODY) {
            return new JournalRepViewHolder(RecyclerRepNoWeightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof RepWeightHeaderViewHolder) {
            RepWeightHeaderViewHolder holder = (RepWeightHeaderViewHolder) viewHolder;
            holder.bindView();
        } else if (viewHolder instanceof RepHeaderViewHolder) {
            RepHeaderViewHolder holder = (RepHeaderViewHolder) viewHolder;
            holder.bindView();
        } else if (viewHolder instanceof JournalRepWeightViewHolder) {
            JournalRepWeightViewHolder holder = (JournalRepWeightViewHolder) viewHolder;
            holder.bindView();
        } else if (viewHolder instanceof JournalRepViewHolder) {
            JournalRepViewHolder holder = (JournalRepViewHolder) viewHolder;
            holder.bindView();
        }
        viewHolder.itemView.setTag(this);
    }

    @Override
    public int getItemCount() {
        return itemList.size() + 1;
    }


    class RepHeaderViewHolder extends RecyclerView.ViewHolder {
        RecyclerRepHeaderBinding binding;

        RepHeaderViewHolder(RecyclerRepHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindView() {
            String type = ormEntity.getInputType() == Constants.EXERCISE_TYPE_WEIGHT ? Constants.SETTINGS_UNIT_MEASURE : "reps";
            binding.repMax.setText(String.format("%% of 1RM (%s %s)", setOneRepMax, type));
        }
    }

    class RepWeightHeaderViewHolder extends RecyclerView.ViewHolder {
        RecyclerRepWeightHeaderBinding binding;

        RepWeightHeaderViewHolder(RecyclerRepWeightHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindView() {
            if (ormEntity != null) {
                String type = ormEntity.getInputType() == Constants.EXERCISE_TYPE_WEIGHT ? Constants.SETTINGS_UNIT_MEASURE : "reps";
                binding.repMax.setText(String.format("%% of 1RM (%s %s)", setOneRepMax, type));
            }
        }
    }

    class JournalRepViewHolder extends RecyclerView.ViewHolder {
        private RecyclerRepNoWeightBinding binding;

        JournalRepViewHolder(RecyclerRepNoWeightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindView() {
            JournalRepEntity repEntity = itemList.get(getAdapterPosition() - 1);
            binding.imageTrophy.setVisibility(repId.equals(repEntity.getId()) ? View.VISIBLE : View.INVISIBLE);
            binding.setSetPos(repEntity.getPosition());
            binding.setRepEntity(repEntity);
            int max = setOneRepMax;
            int progress = repEntity.getOrmInt();
            binding.progressBar.setMax(max);
            binding.progressBar.setProgress(progress);
        }
    }

    class JournalRepWeightViewHolder extends RecyclerView.ViewHolder {
        private RecyclerRepItemBinding binding;

        JournalRepWeightViewHolder(RecyclerRepItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindView() {
            JournalRepEntity repEntity = itemList.get(getAdapterPosition() - 1);
            binding.imageTrophy.setVisibility(repId.equals(repEntity.getId()) ? View.VISIBLE : View.INVISIBLE);
            binding.setSetPos(repEntity.getPosition());
            binding.setRepEntity(repEntity);
            int max = setOneRepMax;
            int progress = repEntity.getOrmInt();
            binding.progressBar.setMax(max);
            binding.progressBar.setProgress(progress);
        }
    }
}