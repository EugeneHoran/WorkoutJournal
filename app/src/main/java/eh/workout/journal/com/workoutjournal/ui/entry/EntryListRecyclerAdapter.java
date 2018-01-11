package eh.workout.journal.com.workoutjournal.ui.entry;


import android.content.Context;
import android.graphics.drawable.Drawable;
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
import eh.workout.journal.com.workoutjournal.databinding.RecyclerEntryItemBinding;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.util.views.LayoutUtil;

public class EntryListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<JournalRepEntity> itemList = new ArrayList<>();

    private EntryAdapterInterface listener;
    private Drawable trophyGray, trophyBlue;

    public EntryListRecyclerAdapter(Context context) {
        LayoutUtil layoutUtil = new LayoutUtil();
        this.trophyGray = layoutUtil.getDrawableMutate(context, R.drawable.ic_trophy, R.color.colorGrayTransparent);
        this.trophyBlue = layoutUtil.getDrawableMutate(context, R.drawable.ic_trophy, R.color.colorAccent);
    }

    public interface EntryAdapterInterface {
        void deleteRep(JournalRepEntity journalRepEntity, List<JournalRepEntity> repEntityList);

        void editRep(JournalRepEntity repEntity);
    }

    public void setListener(EntryAdapterInterface listener) {
        this.listener = listener;
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
                if (oldRep.isShowTopLine() != newRep.isShowTopLine() || oldRep.isShowBottomLine() != newRep.isShowBottomLine()) {
                    return false;
                }
                if (oldRep.isORM() != newRep.isORM()) {
                    return false;
                }
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
        return new RepViewHolder(RecyclerEntryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RepViewHolder repViewHolder = (RepViewHolder) holder;
        repViewHolder.bindView();
        holder.itemView.setTag(this);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class RepViewHolder extends RecyclerView.ViewHolder {
        private RecyclerEntryItemBinding binding;
        private JournalRepEntity repEntity;
        public boolean showWeight = true;

        RepViewHolder(RecyclerEntryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindView() {
            repEntity = itemList.get(getAdapterPosition());
            showWeight = repEntity.getExerciseInputType() == 0;
            binding.setShowTopLine(repEntity.isShowTopLine());
            binding.setShowBottomLine(repEntity.isShowBottomLine());
            binding.setRepEntity(repEntity);
            binding.imageTrophy.setImageDrawable(repEntity.isORM() ? trophyBlue : trophyGray);
            binding.setHolder(this);
        }

        public void onRepClicked(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.END, R.attr.actionOverflowMenuStyle, 0);
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