package net.leseonline.cardinventorymanager;

import android.graphics.Bitmap;

/**
 * Created by mlese on 4/15/2017.
 */
public class CardItem {
    BaseballCard mCard;
    Bitmap mFrontImage;

    public CardItem(BaseballCard card, Bitmap fronImage) {
        super();
        mCard = card;
        mFrontImage = fronImage;
    }

    public BaseballCard getCard() {
        return mCard;
    }

    public void setCard(BaseballCard card) {
        mCard = card;
    }

    public Bitmap getFrontImage() {
        return mFrontImage;
    }

    public void setFrontImage(Bitmap frontImage) {
        mFrontImage = frontImage;
    }
}
