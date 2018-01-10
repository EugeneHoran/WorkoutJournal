package eh.workout.journal.com.workoutjournal.ui.entry;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import eh.workout.journal.com.workoutjournal.databinding.RecyclerHistoryDetailsItemBinding;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerSetItemWithRecyclerBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseOrmEntity;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;
import eh.workout.journal.com.workoutjournal.db.relations.ExerciseSetRepRelation;
import eh.workout.journal.com.workoutjournal.model.OrmHistory;
import eh.workout.journal.com.workoutjournal.ui.shared.RepChildRecyclerAdapter;
import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.MyStringUtil;

public class EntryHistoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_SET = 1;
    private List<Object> itemList = new ArrayList<>();

    void setItems(final List<Object> items) {
        if (items == null) {
            this.itemList.clear();
            notifyDataSetChanged();
            return;
        }
        this.itemList.clear();
        this.itemList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof OrmHistory) {
            return TYPE_HEADER;
        } else {
            return TYPE_SET;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SET) {
            return new EntryHistoryViewHolder(RecyclerSetItemWithRecyclerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new HistoryHeaderViewHolder(RecyclerHistoryDetailsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            HistoryHeaderViewHolder holder = (HistoryHeaderViewHolder) viewHolder;
            holder.bindView();
        } else {
            EntryHistoryViewHolder holder = (EntryHistoryViewHolder) viewHolder;
            holder.bindView();
        }
        viewHolder.itemView.setTag(this);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class HistoryHeaderViewHolder extends RecyclerView.ViewHolder {
        private RecyclerHistoryDetailsItemBinding binding;

        HistoryHeaderViewHolder(RecyclerHistoryDetailsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindView() {
            final OrmHistory ormHistory = (OrmHistory) itemList.get(getAdapterPosition());
            if (ormHistory.getMax() != null) {
                Date d = new Date(ormHistory.getMax().getTimestamp());
                SimpleDateFormat f = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
                binding.pbDate.setText(f.format(d));
                binding.pbWeight.setText(MyStringUtil.formatOneRepMaxWeightFromRep(ormHistory.getMax()));
            }
            if (ormHistory.getLine().getPoints().size() >= 1) {
                initGraph(ormHistory);
            }
        }

        private void initGraph(OrmHistory ormHistory) {
            binding.lineGraph.removeAllLines();
            Line line = ormHistory.getLine();
            line.setColor(Color.BLUE);
            binding.lineGraph.addLine(ormHistory.getLine());
            if (ormHistory.getDateLine() != null) {
                binding.lineGraph.addLine(ormHistory.getDateLine());
            }
            binding.max.setText(String.format("%s %s", Math.round(ormHistory.getMax().getOneRepMax() + 20), Constants.SETTINGS_UNIT_MEASURE));
            binding.min.setText(String.format("%s %s", Math.round(ormHistory.getMin() - 20), Constants.SETTINGS_UNIT_MEASURE));
            binding.mid.setText(String.format("%s %s", Math.round((ormHistory.getMax().getOneRepMax() + ormHistory.getMin()) / 2), Constants.SETTINGS_UNIT_MEASURE));

            binding.lineGraph.setRangeY((float) ormHistory.getMin() - 20, (float) ormHistory.getMax().getOneRepMax() + 20);
            binding.lineGraph.setLineToFill(0);
        }
    }

    public class EntryHistoryViewHolder extends RecyclerView.ViewHolder {
        private RecyclerSetItemWithRecyclerBinding binding;
        private RepChildRecyclerAdapter adapter;
        private JournalSetEntity setEntity;

        EntryHistoryViewHolder(RecyclerSetItemWithRecyclerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            adapter = new RepChildRecyclerAdapter();
        }

        void bindView() {
            ExerciseSetRepRelation dateSetRepRelation = (ExerciseSetRepRelation) itemList.get(getAdapterPosition());
            ExerciseOrmEntity ormEntity = dateSetRepRelation.getExerciseOrmEntity().get(0);
            setEntity = dateSetRepRelation.getJournalSetEntity();
            String title = String.valueOf(DateUtils.getRelativeTimeSpanString(setEntity.getTimestamp()));
            binding.setTitle(title);
            adapter.setOneRepMax(ormEntity);
            binding.recycler.setAdapter(adapter);
            if (dateSetRepRelation.getJournalRepEntityList() != null) {
                if (dateSetRepRelation.getJournalRepEntityList().size() > 0) {
                    adapter.setItems(dateSetRepRelation.getJournalRepEntityList());
                }
            }
            binding.recycler.setLayoutFrozen(true);
            binding.setHideMenu(true);
        }
    }
}
