package eh.workout.journal.com.workoutjournal.ui.entry;


import android.support.v7.util.DiffUtil;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.R;
import eh.workout.journal.com.workoutjournal.databinding.RecyclerAddExerciseEntryBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;

public class EntryListRecyclerAdapter extends RecyclerView.Adapter<EntryListRecyclerAdapter.RepViewHolder> {
    private List<JournalRepEntity> itemList = new ArrayList<>();

    private EntryAdapterInterface listener;

    interface EntryAdapterInterface {
        void deleteRep(JournalRepEntity journalRepEntity, List<JournalRepEntity> repEntityList);

        void editRep(JournalRepEntity repEntity);
    }

    void setListener(EntryAdapterInterface listener) {
        this.listener = listener;
    }


    void setItems(final List<JournalRepEntity> items) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
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
                return itemList.get(oldItemPosition).getId().equals(items.get(newItemPosition).getId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                JournalRepEntity oldRep = itemList.get(oldItemPosition);
                JournalRepEntity newRep = items.get(newItemPosition);
                return oldRep.getId().equals(newRep.getId()) &&
                        oldRep.getReps().equals(newRep.getReps()) &&
                        oldRep.getWeight().equals(newRep.getWeight()) &&
                        oldRep.getTempPosition() == newRep.getTempPosition() &&
                        oldRep.getPosition() == newRep.getPosition();
            }
        });
        this.itemList.clear();
        this.itemList.addAll(items);
        result.dispatchUpdatesTo(this);
    }

    @Override
    public EntryListRecyclerAdapter.RepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RepViewHolder(RecyclerAddExerciseEntryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(RepViewHolder holder, int position) {
        holder.bindView();
        holder.itemView.setTag(this);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class RepViewHolder extends RecyclerView.ViewHolder {
        private RecyclerAddExerciseEntryBinding binding;
        private JournalRepEntity repEntity;

        RepViewHolder(RecyclerAddExerciseEntryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindView() {
            repEntity = itemList.get(getAdapterPosition());
            binding.setRepEntity(repEntity);
            binding.setHolder(this);
        }

        public void onRepClicked(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.END);
            popup.getMenuInflater().inflate(R.menu.menu_edit_rep, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.action_delete) {
                        if (listener != null) {
                            List<JournalRepEntity> newRepList = new ArrayList<>(itemList);
                            newRepList.remove(getAdapterPosition());
                            listener.deleteRep(repEntity, newRepList);
                        }
                    } else if (id == R.id.action_edit) {
                        if (listener != null) {
                            listener.editRep(new JournalRepEntity(repEntity));
                        }
                    }
                    return true;
                }
            });
            popup.show();
        }
    }
}