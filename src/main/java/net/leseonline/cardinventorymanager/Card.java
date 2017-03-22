package net.leseonline.cardinventorymanager;

/**
 * Created by mlese on 3/5/2017.
 */
public abstract class Card {
    private int mUniqueId;
    private String mCompanyName;
    private float mValue;
    private String mNotes;
    private String mFrontImagePath;
    private String mBackImagePath;
    private Condition mCondition;
    private int mCardNum;

    public Card(int uniqueId) {
        mUniqueId = uniqueId;
        mCompanyName = "";
        mValue = 0.0f;
        mNotes = "";
        mFrontImagePath = "";
        mBackImagePath = "";
        mCondition = Condition.None;
    }

    abstract public String getCategoryName();

    public int getUniqueId() {
        return mUniqueId;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public void setCompanyName(String companyName) {
        mCompanyName = companyName;
    }

    public float getValue() {
        return mValue;
    }

    public void setValue(float value) {
        mValue = value;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public String getFrontImagePath() {
        return mFrontImagePath;
    }

    public void setFrontImagePath(String frontImagePath) {
        mFrontImagePath = frontImagePath;
    }

    public String getBackImagePath() {
        return mBackImagePath;
    }

    public void setBackImagePath(String backImagePath) {
        mBackImagePath = backImagePath;
    }

    public Condition getCondition() {
        return mCondition;
    }

    public void setCondition(Condition condition) {
        mCondition = condition;
    }

    public int getCardNum() {
        return mCardNum;
    }

    public void setCardNum(int cardNum) {
        mCardNum = cardNum;
    }

    public void setUniqueId(int uniqueId) {
        mUniqueId = uniqueId;
    }

    /**
     * This enumeration defines the Trading Card 10-point grading scale.
     */
    public enum Condition {
        None(-1, "---"),
        GEM_MT(10, "Gem Mint"),
        MINT(9, "Mint"),
        NM_MT(8, "Near Mint-Mint"),
        NM(7, "Near Mint"),
        EX_MT(6, "Excellent-Mint"),
        EX(5, "Excellent"),
        VG_EX(4, "Very Good-Excellent"),
        VG(3, "Very Good"),
        G(2, "Good"),
        PR_FR(1, "Poor to Fair");

        private int mCode;
        private String mText;

        Condition (int code, String text) {
            mCode = code;
            mText = text;
        }

        public int getCode() {
            return mCode;
        }

        public String getText() {
            return mText;
        }

        @Override
        public String toString() {
            return mText;
        }

        public static String[] getTextValues() {
            Condition[] values = Condition.values();
            String[] texts = new String[values.length];
            for (int n = 0; n < values.length; n++) {
                texts[n] = values[n].getText();
            }
            return texts;
        }

        public static int[] getCodes() {
            Condition[] values = Condition.values();
            int[] codes = new int[values.length];
            for (int n = 0; n < values.length; n++) {
                codes[n] = values[n].getCode();
            }
            return codes;
        }

        public static Condition fromCode(int code) {
            Condition result = Condition.None;
            Condition[] values = Condition.values();
            int n = 0;
            while (n < values.length && result == Condition.None) {
                if (values[n].getCode() == code) {
                    result = values[n];
                }
                n++;
            }
            return  result;
        }
    }
}
