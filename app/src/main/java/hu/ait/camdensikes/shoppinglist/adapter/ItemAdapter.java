package hu.ait.camdensikes.shoppinglist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import hu.ait.camdensikes.shoppinglist.MainActivity;
import hu.ait.camdensikes.shoppinglist.R;
import hu.ait.camdensikes.shoppinglist.data.ListItem;

import static com.orm.SugarRecord.find;

/**
 * Created by Camden Sikes on 11/13/2016.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox check;
        public ImageView ivIcon;
        public TextView tvItem;
        public TextView tvItemDesc;
        public TextView tvPrice;
        public Button btnDelete;
        public Button btnEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            check = (CheckBox) itemView.findViewById(R.id.checkbox);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            tvItem = (TextView) itemView.findViewById(R.id.tvItem);
            tvItemDesc = (TextView) itemView.findViewById(R.id.tvItemDesc);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
        }

    }

    private List<ListItem> itemList;
    private Context context;
    private int lastPosition = -1;

    public ItemAdapter(List<ListItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.tvItem.setText(itemList.get(position).getItemName());
        viewHolder.tvItemDesc.setText(itemList.get(position).getDescription());
        viewHolder.tvPrice.setText(itemList.get(position).getPrice());
        viewHolder.check.setChecked(itemList.get(position).isChecked());
        viewHolder.ivIcon.setImageResource(
                itemList.get(position).getItemType().getIconId());

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePlace(position);
            }
        });
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).showEditItemActivity(itemList.get(position), position);
            }
        });
        viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                itemList.get(position).setChecked(b);
                itemList.set(position, itemList.get(position));
                itemList.get(position).save();
            }
        });

        setAnimation(viewHolder.itemView, position);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItem(ListItem item) {
        item.save();
        itemList.add(item);
        notifyDataSetChanged();
    }

    public void updateItem(int index, ListItem item) {
        itemList.set(index, item);
        item.save();
        notifyItemChanged(index);
    }

    public void removePlace(int index) {
        // remove it from the DB
        itemList.get(index).delete();
        // remove it from the list
        itemList.remove(index);
        notifyDataSetChanged();
    }

    public void swapItems(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(itemList, i, i + 1);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(itemList, i, i - 1);
            }
        }
        notifyDataSetChanged();
    }

    public ListItem getItem(int i) {
        return itemList.get(i);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void clearList() {
        ListItem.deleteAll(ListItem.class);
        itemList.clear();
        notifyDataSetChanged();
    }

    public void clearChecked() {
        ListItem.deleteAll(ListItem.class, "checked = ?", "1");
        itemList = ListItem.listAll(ListItem.class);
        notifyDataSetChanged();
    }

}
