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

public class SingleCardActivityAnim extends AppCompatActivity implements
        SearchDialogFragment.ISearchDialogListener {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private final static int CAPTURE_DATA_REQUEST = 6;
    private ViewFlipper mViewFlipper;
    private Context mContext;
    private ArrayList<Integer> mIndices;
    private int mCurrentIndex;
    private int mCurrentResourceIndex;
    private DatabaseHelper mDatabaseHelper;
    private ArrayList<Long> mIds;
    private ArrayList<Long> mResourceIds;
    private ArrayList<Long> mTextViewIds;
    private int mUniqueCardId;
    private boolean mIsFront;
    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;
        mIndices = new ArrayList<>();
        mResourceIds = new ArrayList<>();
        mTextViewIds = new ArrayList<>();
        mCurrentIndex = 0;
        mCurrentResourceIndex = 0;
        mUniqueCardId = 0;
        mIsFront = true;
        mDatabaseHelper = new DatabaseHelper(this);
        mIds = mDatabaseHelper.search();

        int extraUniqueId = this.getIntent().getIntExtra(getResources().getString(R.string.extra_unique_id), -1);
        mUniqueCardId = (extraUniqueId == -1) ? mUniqueCardId : extraUniqueId;

        mCurrentIndex = mIds.indexOf((long)mUniqueCardId);
        if (mCurrentIndex == -1) {
            mCurrentIndex = 0;
        }

        mResourceIds.add((long)R.id.view_anim_left);
        mResourceIds.add((long)R.id.view_anim_center);
        mResourceIds.add((long)R.id.view_anim_right);

        mTextViewIds.add((long)R.id.text_view_anim_left);
        mTextViewIds.add((long)R.id.text_view_anim_center);
        mTextViewIds.add((long)R.id.text_view_anim_right);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            FragmentManager fm = getFragmentManager();
            SearchDialogFragment dialogFragment = new SearchDialogFragment();
            dialogFragment.show(fm, "Search");
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_DATA_REQUEST) {
            if (resultCode == RESULT_OK) {
                BaseballCard card = CaptureDataActivity.getCard();
                mDatabaseHelper.updateCard(card);
            }
        }
    }

    private void setCard(int cardIdx, int resourceIdx) {
        // Views are a sliding window.
        //      currentIndex-1 -> currentResourceIndex-1
        //      currentIndex -> currentResourceIndex
        //      currentIndex+1 -> currentResourceIndex+1
        BaseballCard card = mDatabaseHelper.find(mIds.get(cardIdx));
        if (card != null) {
            boolean isFiltered = mDatabaseHelper.getSearchModel().isFiltered();
            mUniqueCardId = card.getUniqueId();
            File file = getImageFile(mIsFront);
            ImageView iv = (ImageView)findViewById(mResourceIds.get(resourceIdx).intValue());
            iv.setImageDrawable(Drawable.createFromPath(file.getPath()));
            String title = card.getFullName();
            title += mIsFront ? " - Front View" : " - Back View";
            title += String.format(" (%d of %d) %s", cardIdx + 1, mIds.size(), isFiltered ? "Filtered" : "");
            TextView tv = (TextView)findViewById(mTextViewIds.get(resourceIdx).intValue());
            tv.setText(title);
        }
    }

    private void setCards() {
        if (mIds.size() > 0) {
            setCard(decrement(mCurrentIndex, mIds), decrement(mCurrentResourceIndex, mResourceIds));
            setCard(mCurrentIndex, mCurrentResourceIndex);
            setCard(increment(mCurrentIndex, mIds), increment(mCurrentResourceIndex, mResourceIds));
        }
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
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            mIsFront = !mIsFront;
            setCards();
            return true;
        }


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

                // top to down swipe or down to top swipe
                if ((e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) ||
                    (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) )
                {
                    // show data
                    Utilities.playClick(SingleCardActivityAnim.this);
//                    Intent intent = new Intent(SingleCardActivityAnim.this, CaptureDataActivity.class);
//                    intent.putExtra(getResources().getString(R.string.extra_unique_id), -mUniqueCardId);
//                    startActivityForResult(intent, CAPTURE_DATA_REQUEST);

                    int idx = mIds.get(mCurrentIndex).intValue();
                    Bundle bundle = new Bundle();
                    bundle.putInt("uniqueId", -idx);
                    FragmentManager fm = getFragmentManager();
                    CaptureDataDialogFragment fragment = new CaptureDataDialogFragment();
                    fragment.setArguments(bundle);
                    fragment.show(fm, "Capture Data");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    @Override
    public void onSearchDialogPositiveAction(SearchDialogFragment dialog) {
        SearchModel model = dialog.getSearchModel();
        mDatabaseHelper.saveSearchModel(model);
        mIds = mDatabaseHelper.search();
        mCurrentIndex = 0;
        mUniqueCardId = 0;
        mCurrentResourceIndex = 0;
        mIsFront = true;
        setCards();
    }

    @Override
    public void onSearchDialogNegativeAction(SearchDialogFragment dialog) {
        // do nothing
    }

    private File getImageFile(Boolean isFront) {
        File filesDir = this.getFilesDir();
        String pre = isFront ? "IMGF_" : "IMGB_";
        File photoFile = new File(filesDir, pre + String.valueOf(Math.abs(mUniqueCardId)) + ".jpg");
        return photoFile;
    }

}
