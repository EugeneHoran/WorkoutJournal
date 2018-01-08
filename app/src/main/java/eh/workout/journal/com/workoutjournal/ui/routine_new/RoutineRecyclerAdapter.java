package eh.workout.journal.com.workoutjournal.ui.routine_new;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.db.CustomTypeConverters;
import eh.workout.journal.com.workoutjournal.model.DaySelector;
import eh.workout.journal.com.workoutjournal.util.DataHelper;


public class RoutineRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DaySelector> itemList = new ArrayList<>();
    private boolean showCheckBox;

    public RoutineRecyclerAdapter(boolean showCheckBox, boolean fromEdit) {
        this.showCheckBox = showCheckBox;
        if (!fromEdit) {
            if (showCheckBox) {
                setItems(new DataHelper().getDays());
            }
        }
    }

    public void setItems(List<DaySelector> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    public List<DaySelector> getItemList() {
        return itemList;
    }

    public List<DaySelector> getSelectedList() {
        List<DaySelector> selectorList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).isSelected()) {
                selectorList.add(itemList.get(i));
            }
        }
        return selectorList;
    }

    public String getDaysString() {
        List<Integer> daySelectorList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).isSelected()) {
                daySelectorList.add(itemList.get(i).getDayInt());
            }
        }
        Integer[] integers = new Integer[daySelectorList.size()];
        for (int i = 0; i < daySelectorList.size(); i++) {
            integers[i] = daySelectorList.get(i);
        }
        return CustomTypeConverters.fromDayListToString(integers);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderTest(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_add_plan_lift_item, parent, false));
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

    class ViewHolderTest extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        DaySelector daySelector;
        TextView liftName;
        CheckBox checkBox;

        ViewHolderTest(View itemView) {
            super(itemView);
            liftName = itemView.findViewById(R.id.liftName);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setVisibility(showCheckBox ? View.VISIBLE : View.GONE);
            itemView.findViewById(R.id.liftParent).setOnClickListener(this);
        }

        private void bindView() {
            daySelector = itemList.get(getAdapterPosition());
            liftName.setText(daySelector.getDayName());
            checkBox.setChecked(daySelector.isSelected());
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View view) {
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
            } else {
                checkBox.setChecked(true);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            daySelector.setSelected(b);
        }
    }
}
