package hu.ait.camdensikes.shoppinglist.data;

import android.content.ClipData;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Date;

import hu.ait.camdensikes.shoppinglist.R;

/**
 * Created by Camden Sikes on 11/13/2016.
 */

public class ListItem extends SugarRecord<ListItem> implements Serializable {

    public enum ItemType {
        FOOD(0, R.drawable.food),
        APPLIANCE(1, R.drawable.appliance), CLOTHING(2, R.drawable.clothing);

        private int value;
        private int iconId;

        private ItemType(int value, int iconId) {
            this.value = value;
            this.iconId = iconId;
        }

        public int getValue() {
            return value;
        }

        public int getIconId() {
            return iconId;
        }

        public static ItemType fromInt(int value) {
            for (ItemType p : ItemType.values()) {
                if (p.value == value) {
                    return p;
                }
            }
            return FOOD;
        }
    }

    private boolean checked;
    private String itemName;
    private String description;
    private String price;
    private ItemType itemType;

    public ListItem() {

    }

    public ListItem(String itemName, String description, String price, ItemType itemType) {
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.itemType = itemType;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String placeName) {
        this.itemName = placeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }
}
