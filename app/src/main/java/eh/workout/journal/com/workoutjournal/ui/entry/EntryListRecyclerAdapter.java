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
import eh.workout.journal.com.workoutjournal.databinding.RecyclerAddExerciseExpandedBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;

public class EntryListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_EXPANDED = 1;

    private List<JournalRepEntity> itemList = new ArrayList<>();

    private EntryAdapterInterface listener;

    public interface EntryAdapterInterface {
        void deleteRep(JournalRepEntity journalRepEntity, List<JournalRepEntity> repEntityList);

        void editRep(JournalRepEntity repEntity);
    }

    public void setListener(EntryAdapterInterface listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position).isSelected()) {
            return TYPE_EXPANDED;
        } else {
            return TYPE_NORMAL;
        }
    }

    void setItems(final List<JournalRepEntity> items) {
        if (items == null) {
            this.itemList.clear();
            notifyDataSetChanged();
            return;
        }

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

                if (oldRep.getWeight() == null || newRep.getWeight() == null) {
                    return oldRep.isSelected() == newRep.isSelected() &&
                            oldRep.getId().equals(newRep.getId()) &&
                            oldRep.getReps().equals(newRep.getReps()) &&
                            oldRep.getTempPosition() == newRep.getTempPosition() &&
                            oldRep.getPosition() == newRep.getPosition();
                } else {
                    return oldRep.isSelected() == newRep.isSelected() &&
                            oldRep.getId().equals(newRep.getId()) &&
                            oldRep.getReps().equals(newRep.getReps()) &&
                            oldRep.getWeight().equals(newRep.getWeight()) &&
                            oldRep.getTempPosition() == newRep.getTempPosition() &&
                            oldRep.getPosition() == newRep.getPosition();
                }
            }
        });
        this.itemList.clear();
        this.itemList.addAll(items);
        result.dispatchUpdatesTo(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            return new RepViewHolder(RecyclerAddExerciseEntryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_EXPANDED) {
            return new RepExpandedViewHolder(RecyclerAddExerciseExpandedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            RepViewHolder repViewHolder = (RepViewHolder) holder;
            repViewHolder.bindView();
        } else {
            RepExpandedViewHolder repViewHolder = (RepExpandedViewHolder) holder;
            repViewHolder.bindView();
        }
        holder.itemView.setTag(this);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class RepExpandedViewHolder extends RecyclerView.ViewHolder {
        private RecyclerAddExerciseExpandedBinding binding;
        private JournalRepEntity repEntity;
        public boolean showWeight = true;

        RepExpandedViewHolder(RecyclerAddExerciseExpandedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindView() {
            repEntity = itemList.get(getAdapterPosition());
            showWeight = repEntity.getExerciseInputType() == 0;
            binding.setRepEntity(repEntity);
            binding.setHolder(this);
            binding.cardHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    expand(getAdapterPosition());
                }
            });
        }

        public void onRepClicked(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.END);
            popup.getMenuInflater().inflate(R.menu.menu_edit_move_delete, popup.getMenu());
            popup.getMenu().findItem(R.id.action_move).setVisible(false);
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

    private void expand(int pos) {
        if (itemList.get(pos).isSelected()) {
            itemList.get(pos).setSelected(false);
            notifyItemChanged(pos);
            return;
        }
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).isSelected()) {
                itemList.get(i).setSelected(false);
                notifyItemChanged(i);
            }
        }
        itemList.get(pos).setSelected(!itemList.get(pos).isSelected());
        notifyItemChanged(pos);
    }

    public class RepViewHolder extends RecyclerView.ViewHolder {
        private RecyclerAddExerciseEntryBinding binding;
        private JournalRepEntity repEntity;
        public boolean showWeight = true;

        RepViewHolder(RecyclerAddExerciseEntryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindView() {
            repEntity = itemList.get(getAdapterPosition());
            showWeight = repEntity.getExerciseInputType() == 0;
            binding.setRepEntity(repEntity);
            binding.setHolder(this);
            binding.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    expand(getAdapterPosition());
                }
            });
        }

        public void onRepClicked(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.END);
            popup.getMenuInflater().inflate(R.menu.menu_edit_move_delete, popup.getMenu());
            popup.getMenu().findItem(R.id.action_delete).setVisible(true);
            popup.getMenu().findItem(R.id.action_edit).setVisible(true);
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