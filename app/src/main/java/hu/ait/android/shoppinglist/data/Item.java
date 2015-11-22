package hu.ait.android.shoppinglist.data;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.math.BigDecimal;

import hu.ait.android.shoppinglist.R;

/**
 * Created by Jason on 11/11/15.
 */
public class Item extends SugarRecord<Item> implements Serializable {
    public enum ItemType {
        FOOD(0, R.drawable.food),
        ELECTRONICS(1, R.drawable.electronics),
        BOOKS(2, R.drawable.books),
        CLOTHES(3, R.drawable.clothes),
        STATIONERY(4, R.drawable.stationery),
        MISCELLANEOUS(5, R.drawable.miscellaneous);

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

    private ItemType itemType;
    private String itemName;
    private String itemDescription;
    private String estimatedPrice;  // Sorry for the very basic implementation, I was trying to use BigDecimal but time was not on my side
    private boolean isBought;

    public Item() {
    }

    public Item(ItemType itemType, String itemName, String itemDescription, String estimatedPrice, boolean isBought) {
        this.itemType = itemType;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.estimatedPrice = estimatedPrice;
        this.isBought = isBought;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(String estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public boolean isBought() {
        return isBought;
    }

    public void setIsBought(boolean isBought) {
        this.isBought = isBought;
    }
}
