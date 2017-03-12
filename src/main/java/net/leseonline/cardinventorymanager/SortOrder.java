package net.leseonline.cardinventorymanager;

/**
 * Created by mlese on 3/11/2017.
 */
public class SortOrder {
    private int resId;
    private int switchResId;
    private int sortOrder;
    private boolean isEnabled;
    private boolean isDesc;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getSwitchResId() {
        return switchResId;
    }

    public void setSwitchResId(int switchResId) {
        this.switchResId = switchResId;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isDesc() {
        return isDesc;
    }

    public void setIsDesc(boolean isDesc) {
        this.isDesc = isDesc;
    }
}
