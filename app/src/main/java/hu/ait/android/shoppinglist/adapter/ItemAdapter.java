package hu.ait.android.shoppinglist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import hu.ait.android.shoppinglist.MainActivity;
import hu.ait.android.shoppinglist.R;
import hu.ait.android.shoppinglist.data.Item;

/**
 * Created by Jason on 11/11/15.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivIcon;
        public TextView tvItem;
        public TextView tvPrice;
        public Button btnDelete;
        public Button btnEdit;
        public Button btnDescription;
        public CheckBox cbBought;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            tvItem = (TextView) itemView.findViewById(R.id.tvItem);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            btnDescription = (Button) itemView.findViewById(R.id.btnDescription);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            cbBought = (CheckBox) itemView.findViewById(R.id.cbBought);
        }
    }

    private List<Item> itemsList;
    private Context context;
    private int lastPosition = -1;

    public ItemAdapter(List<Item> placesList, Context context) {
        this.itemsList = placesList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.tvItem.setText(itemsList.get(position).getItemName());
        viewHolder.tvPrice.setText(itemsList.get(position).getEstimatedPrice());
        viewHolder.cbBought.setChecked(itemsList.get(position).isBought());
        viewHolder.cbBought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsList.get(position).setIsBought(((CheckBox) v).isChecked());
                itemsList.get(position).save();

                ((MainActivity) context).showCheckBoxClickedActivity(itemsList.get(position).isBought());
            }
        });
        viewHolder.ivIcon.setImageResource(
                itemsList.get(position).getItemType().getIconId());
        viewHolder.btnDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).showDescriptionItemActivity(itemsList.get(position));
            }
        });
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).showEditItemActivity(itemsList.get(position), position);
            }
        });

        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void addItem(Item item) {
        item.save();
        itemsList.add(item);
        notifyDataSetChanged();
    }

    public void updateItem(int index, Item item) {
        itemsList.set(index, item);
        item.save();
        notifyItemChanged(index);
    }

    public void removeAllItems() {
        int size = getItemCount();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                itemsList.get(0).delete();
                itemsList.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }
    
    public void removeItem(int index) {
        // remove it from the DB
        itemsList.get(index).delete();
        // remove it from the list
        itemsList.remove(index);
        notifyDataSetChanged();
    }

    public void swapItems(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(itemsList, i, i + 1);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(itemsList, i, i - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
    }

    public int findTotalCost() {
        int sum = 0;
        if (getItemCount() > 0) {
            for (int i = 0; i < getItemCount(); i++) {
                int temp = Integer.parseInt(getItem(i).getEstimatedPrice());
                sum = sum + temp;
            }
        }
        return sum;
    }

    public Item getItem(int i) {
        return itemsList.get(i);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
