package net.leseonline.cardinventorymanager;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import net.leseonline.cardinventorymanager.db.DatabaseHelper;

import java.io.File;
import java.util.ArrayList;

public class SingleCardActivityAnim extends AppCompatActivity implements SearchDialogFragment.ISearchDialogListener {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ViewFlipper mViewFlipper;
    private Context mContext;
    private ArrayList<Integer> mIndices;
    private int mCurrentIndex;
    private int mCurrentResourceIndex;
    private DatabaseHelper mDatabaseHelper;
    private ArrayList<Long> mIds;
    private ArrayList<Long> mResourceIds;
    private int mUniqueCardId;
    private boolean mIsFront;
    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_card);
        mContext = this;
        mIndices = new ArrayList<>();
        mResourceIds = new ArrayList<>();
        mCurrentIndex = 0;
        mCurrentResourceIndex = 0;
        mUniqueCardId = 0;
        mIsFront = true;
        mDatabaseHelper = new DatabaseHelper(this);
        mIds = mDatabaseHelper.search();

        mResourceIds.add((long) R.id.view_anim_left);
        mResourceIds.add((long) R.id.view_anim_center);
        mResourceIds.add((long) R.id.view_anim_right);

        setCards();
        mViewFlipper = (ViewFlipper) this.findViewById(R.id.view_flipper);
        mViewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void setCard(int cardIdx, int resourceIdx) {
        // Views are a sliding window.
        //      currentIndex-1 -> currentResourceIndex-1
        //      currentIndex -> currentResourceIndex
        //      currentIndex+1 -> currentResourceIndex+1
        if (mIds.size() > 0) {
            BaseballCard card = mDatabaseHelper.find(mIds.get(cardIdx));
            if (card != null) {
                boolean isFiltered = mDatabaseHelper.getSearchModel().isFiltered();
                mUniqueCardId = card.getUniqueId();
                File file = getImageFile(mIsFront);
                ImageView iv = (ImageView)findViewById(mResourceIds.get(resourceIdx).intValue());
                iv.setImageDrawable(Drawable.createFromPath(file.getPath()));
            }
        }
    }

    private void setCards() {
        setCard(decrement(mCurrentIndex, mIds), decrement(mCurrentResourceIndex, mResourceIds));
        setCard(mCurrentIndex, mCurrentResourceIndex);
        setCard(increment(mCurrentIndex, mIds), increment(mCurrentResourceIndex, mResourceIds));
    }

    private int increment(int idx, ArrayList<Long> array) {
        int result = (idx + 1) % array.size();
        return result;
    }

    private int decrement(int idx, ArrayList<Long> array) {
        int result = idx - 1;
        result = (result >= 0) ? result : (array.size() - 1);
        return result;
    }

    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));
                    mViewFlipper.showNext();
                    mCurrentIndex = increment(mCurrentIndex, mIds);
                    mCurrentResourceIndex = increment(mCurrentResourceIndex, mResourceIds);
                    setCards();
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,R.anim.right_out));
                    mViewFlipper.showPrevious();
                    mCurrentIndex = decrement(mCurrentIndex, mIds);
                    mCurrentResourceIndex = decrement(mCurrentResourceIndex, mResourceIds);
                    setCards();
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    @Override
    public void onSearchDialogPositiveAction(SearchDialogFragment dialog) {

    }

    @Override
    public void onSearchDialogNegativeAction(SearchDialogFragment dialog) {

    }

    private File getImageFile(Boolean isFront) {
        File filesDir = this.getFilesDir();
        String pre = isFront ? "IMGF_" : "IMGB_";
        File photoFile = new File(filesDir, pre + String.valueOf(Math.abs(mUniqueCardId)) + ".jpg");
        return photoFile;
    }

}
