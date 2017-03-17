package net.leseonline.cardinventorymanager;

import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import net.leseonline.cardinventorymanager.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashSet;

public class SingleCardActivity extends AppCompatActivity implements SearchDialogFragment.ISearchDialogListener {
    private GestureDetectorCompat mDetector;
    private ArrayList<Integer> mIndices;
    private int mCurrentIndex;
    private Bitmap[] mBitmaps;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mIndices = new ArrayList<>();
        mCurrentIndex = 0;
        mDatabaseHelper = new DatabaseHelper(this);

        mBitmaps = new Bitmap[] {
                BitmapFactory.decodeResource(getResources(), R.drawable.bc1),
                BitmapFactory.decodeResource(getResources(), R.drawable.bc2),
                BitmapFactory.decodeResource(getResources(), R.drawable.bc3),
                BitmapFactory.decodeResource(getResources(), R.drawable.bc4),
                BitmapFactory.decodeResource(getResources(), R.drawable.bc5),
                BitmapFactory.decodeResource(getResources(), R.drawable.bc6),
                BitmapFactory.decodeResource(getResources(), R.drawable.bc7),
                BitmapFactory.decodeResource(getResources(), R.drawable.bc8),
                BitmapFactory.decodeResource(getResources(), R.drawable.bc9),
                BitmapFactory.decodeResource(getResources(), R.drawable.bc10),
                BitmapFactory.decodeResource(getResources(), R.drawable.bc11),
        };

        while (mIndices.size() != mBitmaps.length) {
            int index = ((int)(Math.random() * 10000)) % mBitmaps.length;
            if (!mIndices.contains(index)) {
                mIndices.add(index);
            }
        }

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        ImageView iv = (ImageView)findViewById(R.id.single_card_view);
        iv.setImageBitmap(mBitmaps[mCurrentIndex]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
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
        if (id == R.id.action_rotate) {
            return true;
        } else if (id == R.id.action_search) {
            FragmentManager fm = getFragmentManager();
            SearchDialogFragment dialogFragment = new SearchDialogFragment();
            dialogFragment.show(fm, "Search");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchDialogPositiveAction(SearchDialogFragment dialog) {
        SearchModel model = dialog.getSearchModel();
        mDatabaseHelper.saveSearchModel(model);
    }

    @Override
    public void onSearchDialogNegativeAction(SearchDialogFragment dialog) {
        // do nothing
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG,"onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            final int MIN_DELTA = 100;
            Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
            float x1 = event1.getX();
            float x2 = event2.getX();
            float delta = x1 - x2;
            if (Math.abs(delta) > MIN_DELTA) {
                if (x1 > x2) {
                    // swipe left, next
                    mCurrentIndex = (mCurrentIndex + 1) % mBitmaps.length;
                } else {
                    // swipe right, prev
                    mCurrentIndex--;
                    if (mCurrentIndex < 0) {
                        mCurrentIndex = mBitmaps.length - 1;
                    }
                }
                ImageView iv = (ImageView)findViewById(R.id.single_card_view);
                iv.setImageBitmap(mBitmaps[mCurrentIndex]);
            }
            return true;
        }
    }
}
