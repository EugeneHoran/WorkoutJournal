package eh.workout.journal.com.workoutjournal.ui.orm;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.databinding.RecyclerOneRepMaxItemBinding;
import eh.workout.journal.com.workoutjournal.util.Constants;
import eh.workout.journal.com.workoutjournal.util.OrmHelper;

public class OneRepMaxRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> itemList = new ArrayList<>();
    private int which;

    OneRepMaxRecyclerAdapter(int which) {
        this.which = which;
        setItems(null);
    }

    void setItems(final List<String> items) {
        this.itemList.clear();
        if (which == Constants.ORM_ONE_REP_MAX) {
            this.itemList.addAll(items == null ? OrmHelper.ormEmptyList() : items);
        } else {
            this.itemList.addAll(items == null ? OrmHelper.percentageEmptyList() : items);
        }
        notifyItemRangeChanged(0, getItemCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderTest(RecyclerOneRepMaxItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
        if (which == Constants.ORM_ONE_REP_MAX) {
            return 12;
        } else {
            return 24;
        }
    }

    private class ViewHolderTest extends RecyclerView.ViewHolder {
        private RecyclerOneRepMaxItemBinding binding;

        ViewHolderTest(RecyclerOneRepMaxItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bindView() {
            if (which == Constants.ORM_ONE_REP_MAX) {
                String amount = "";
                if (itemList.get(getAdapterPosition()).equalsIgnoreCase("0")) {
                    amount = "<font color='#000000'>" + itemList.get(getAdapterPosition()) + "<small> " + Constants.SETTINGS_UNIT_MEASURE + "</small></font>";
                } else {
                    amount = "<font color='#0039cb'>" + itemList.get(getAdapterPosition()) + "<small> " + Constants.SETTINGS_UNIT_MEASURE + "</small></font>";
                }
                binding.setPosition(Html.fromHtml(String.valueOf(getAdapterPosition() + 1) + "<small> Reps</small>"));
                binding.setOrm(Html.fromHtml(amount));
            } else {
                binding.setPosition(Html.fromHtml(String.format("%s%% of 1RM", OrmHelper.getMaxPercentages(getAdapterPosition()))));
                binding.setOrm(Html.fromHtml(String.format("%s Lbs", itemList.get(getAdapterPosition()))));
            }
        }
    }
}
