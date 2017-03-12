package net.leseonline.cardinventorymanager;

/**
 * Created by mlese on 3/11/2017.
 */
public class SortOrder {
    private String friendlyName;
    private int enabledResId;
    private int descendingResId;
    private int sortOrder;
    private boolean isEnabled;
    private boolean isDesc;

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public int getEnabledResId() {
        return enabledResId;
    }

    public void setEnabledResId(int enabledResId) {
        this.enabledResId = enabledResId;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public int getDescendingResId() {
        return descendingResId;
    }

    public void setDescendingResId(int descendingResId) {
        this.descendingResId = descendingResId;
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
