package net.leseonline.cardinventorymanager;

/**
 * Created by mlese on 3/11/2017.
 */
public class EffectState {
    private String friendlyName;
    private int resId;
    private boolean isOn;

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }
}
