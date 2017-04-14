package net.leseonline.cardinventorymanager;

/**
 * Created by mlese on 3/5/2017.
 */
public abstract class SportsCard extends Card {
    private String mLastName;
    private String mFirstName;
    private String mTeamName;
    private int mYear;

    public SportsCard(int uniqueId) {
        super(uniqueId);
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getTeamName() {
        return mTeamName;
    }

    public void setTeamName(String teamName) {
        mTeamName = teamName;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}
