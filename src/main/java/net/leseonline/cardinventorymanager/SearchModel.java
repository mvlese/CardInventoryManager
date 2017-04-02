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
    int mCardNum;

    public SearchModel() {
        mFirstName = "";
        mLastName = "";
        mTeamName = "";
        mCompany = "";
        mPosition = BaseballCard.Position.None;
        mCondition = Card.Condition.None;
        mYear = Integer.MIN_VALUE;
        mCardNum = Integer.MIN_VALUE;
    }

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

    public int getCardNum() {
        return mCardNum;
    }

    public void setCardNum(int cardNum) {
        mCardNum = cardNum;
    }

    public boolean isFiltered() {
        boolean result =
            (mFirstName != null && mFirstName.length() > 0) ||
            (mLastName != null && mLastName.length() > 0) ||
            (mCompany != null && mCompany.length() > 0) ||
            (mTeamName != null && mTeamName.length() > 0) ||
            mCondition != Card.Condition.None ||
            mPosition != BaseballCard.Position.None ||
            mYear != Integer.MIN_VALUE ||
            mCardNum != Integer.MIN_VALUE;
        return result;
    }
}
