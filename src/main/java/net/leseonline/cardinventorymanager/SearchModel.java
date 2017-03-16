package net.leseonline.cardinventorymanager;

/**
 * Created by mlese on 3/16/2017.
 */
public class SearchModel {
    String mFirstName;
    String mLastName;
    String mTeamName;
    String mCompany;
    BaseballCard.Position mPosition;
    Card.Condition mCondition;
    int mYear;

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getTeamName() {
        return mTeamName;
    }

    public void setTeamName(String teamName) {
        mTeamName = teamName;
    }

    public String getCompany() {
        return mCompany;
    }

    public void setCompany(String company) {
        mCompany = company;
    }

    public BaseballCard.Position getPosition() {
        return mPosition;
    }

    public void setPosition(BaseballCard.Position position) {
        mPosition = position;
    }

    public Card.Condition getCondition() {
        return mCondition;
    }

    public void setCondition(Card.Condition condition) {
        mCondition = condition;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }
}
