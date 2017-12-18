package eh.workout.journal.com.workoutjournal.ui.plan;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.db.entinty.ExerciseLiftEntity;

public class AddPlanLiftRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private List<ExerciseLiftEntity> itemList = new ArrayList<>();
    private List<ExerciseLiftEntity> itemListFiltered = new ArrayList<>();

    public void setItems(List<ExerciseLiftEntity> itemList) {
        this.itemList.addAll(itemList);
        this.itemListFiltered.addAll(itemList);
        notifyDataSetChanged();
    }

    public List<ExerciseLiftEntity> getSelectedList() {
        List<ExerciseLiftEntity> selectedList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).isSelected()) {
                selectedList.add(itemList.get(i));
            }
        }
        return selectedList;
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
        return itemListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itemListFiltered = itemList;
                } else {
                    List<ExerciseLiftEntity> filteredList = new ArrayList<>();
                    for (ExerciseLiftEntity row : itemList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    itemListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = itemListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemListFiltered = (ArrayList<ExerciseLiftEntity>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolderTest extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        TextView liftName;
        CheckBox checkBox;
        private ExerciseLiftEntity exerciseLiftEntity;

        ViewHolderTest(View itemView) {
            super(itemView);
            liftName = itemView.findViewById(R.id.liftName);
            checkBox = itemView.findViewById(R.id.checkbox);
            itemView.findViewById(R.id.liftParent).setOnClickListener(this);
        }

        private void bindView() {
            exerciseLiftEntity = itemListFiltered.get(getAdapterPosition());
            liftName.setText(exerciseLiftEntity.getName());
            checkBox.setChecked(exerciseLiftEntity.isSelected());
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            for (int i = 0; i < itemList.size(); i++) {
                if (itemList.get(i).getId().equalsIgnoreCase(exerciseLiftEntity.getId())) {
                    itemList.get(i).setSelected(checkBox.isChecked());
                }
            }
            exerciseLiftEntity.setSelected(checkBox.isChecked());
        }

        @Override
        public void onClick(View view) {
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
            } else {
                checkBox.setChecked(true);
            }
        }
    }
}
