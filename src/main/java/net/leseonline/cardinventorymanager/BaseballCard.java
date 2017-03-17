package net.leseonline.cardinventorymanager;

/**
 * Created by mlese on 3/5/2017.
 */
public class BaseballCard extends SportsCard {

    public BaseballCard(int uniqueId) {
        super(uniqueId);
    }

    private Position mPosition;

    @Override
    public String getCategoryName() {
        return "Baseball";
    }

    public Position getPosition() {
        return mPosition;
    }

    public void setPosition(Position position) {
        mPosition = position;
    }

    @Override
    public String toString() {
        String result =
            "Unique ID: " + getUniqueId() + "~" +
            "Category: " + getCategoryName() + "~" +
            "First Name: " + getFirstName() + "~" +
            "Last Name: " + getLastName() + "~" +
            "Team: " + getTeamName() + "~" +
            "Company: " + getCompanyName() + "~" +
            "Value: " + String.valueOf(getValue()) + "~" +
            "Year: " + String.valueOf(getYear()) + "~" +
            "Condition: " + getCondition().getText() + "~" +
            "Position: " + getPosition().getText() + "~" +
            "Notes: " + getNotes();
        return result;
    }

    public enum Position {
        None(-1, "---"),
        Pitcher(1, "P"),
        Catcher(2, "C"),
        FirstBase(3, "1B"),
        SecondBase(4, "2B"),
        ThirdBase(5, "3B"),
        Shortstop(6, "SS"),
        LeftField(7, "LF"),
        CenterField(8, "CF"),
        RightField(9, "RF"),
        Outfield(99, "OF"),
        DesignatedHitter(0, "DH");

        private int mNumeric;
        private String mText;
        Position(int numeric, String text) {
            mNumeric = numeric;
            mText = text;
        }

        public int getCode() {
            return mNumeric;
        }

        public String getText() {
            return mText;
        }

        @Override
        public String toString() {
            return mText;
        }

        public static String[] getTextValues() {
            Position[] values = Position.values();
            String[] texts = new String[values.length];
            for (int n = 0; n < values.length; n++) {
                texts[n] = values[n].getText();
            }
            return texts;
        }

        public static int[] getCodes() {
            Position[] values = Position.values();
            int[] codes = new int[values.length];
            for (int n = 0; n < values.length; n++) {
                codes[n] = values[n].getCode();
            }
            return codes;
        }

        public static Position fromCode(int code) {
            Position result = Position.None;
            Position[] values = Position.values();
            int n = 0;
            while (n < values.length && result == Position.None) {
                if (values[n].getCode() == code) {
                    result = values[n];
                }
                n++;
            }
            return  result;
        }

    }

}
