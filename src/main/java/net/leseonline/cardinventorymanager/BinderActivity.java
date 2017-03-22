package net.leseonline.cardinventorymanager;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import net.leseonline.cardinventorymanager.db.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;

public class BinderActivity extends AppCompatActivity implements SearchDialogFragment.ISearchDialogListener {
    private int mDisplayWidth;
    private int mDisplayHeight;
    private int mStatusBarHeight;
    private int mRequiredHeight;
    private int mColumnWidth;
    private int mColumnHeight;
    private GridView mGridView;
    private GestureDetectorCompat mDetector;
    private String[] mStrArr;
    private DatabaseHelper mDatabaseHelper;

    private static int Width = 586;
    private static int Height = 784;
    private static int LeftOffset = 130;
    private static int TopOffset = 30;
    private static int RightTickMark = 558;
    private final MediaPlayer mMediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDatabaseHelper = new DatabaseHelper(this);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDisplayWidth = metrics.widthPixels ;
        mDisplayHeight = metrics.heightPixels;

        mStatusBarHeight = getStatusBarHeight(getApplicationContext());
        mRequiredHeight = mDisplayHeight - mStatusBarHeight;

        float xScale = mDisplayWidth / (float)Width;
        float yScale = mRequiredHeight / (float)Height;
        int leftPad = (int)(LeftOffset * xScale + 0.5);
        int rightPad = (int)((Width - RightTickMark) * xScale + 0.5);
        int topPad = (int)(TopOffset * yScale + 0.5);

        mDisplayWidth -= leftPad + rightPad;
        mRequiredHeight -= topPad * 2;

        int column = 3;
        mGridView = (GridView) findViewById(R.id.gridView1);
        mGridView.setPadding(leftPad, topPad, rightPad, topPad);

        mGridView.setNumColumns(column);// set your  column number what you want
        int arrSize = column * column ;
        mStrArr = new String[arrSize];
        for(int i = 0; i < arrSize; i++){
            mStrArr[i] = String.valueOf(i);
        }
        mColumnWidth = mDisplayWidth / column ;
        mColumnHeight = mRequiredHeight / column ;
        mGridView.setAdapter(new ImageAdapter(this, mStrArr, mColumnWidth, mColumnHeight));
        enableDisableView(mGridView, false);
        mGridView.setSoundEffectsEnabled(true);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Log.d("Gestures", "onItemClick");
//                Toast.makeText(
//                        getApplicationContext(),
//                        String.valueOf(position), Toast.LENGTH_SHORT).show();

            }
        });
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        Log.d("Gestures", "onTouch: " + event.toString());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_admin) {
            // show password dialog
            // if entered password equals admin password, then delete DB and image files.
//            Toast.makeText(MainActivity.this.getApplicationContext(), "Logon to Destroy Database", Toast.LENGTH_SHORT).show();
            FragmentManager fm = getFragmentManager();
            AdminPwDialogFragment dialogFragment = new AdminPwDialogFragment();
            dialogFragment.show(fm, "EnterAdminPw");

            return true;
        } else if (id == R.id.action_search) {
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

    public void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);

        if ( view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;

            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }

    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return result + mActionBarSize;
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
            Log.d(DEBUG_TAG, "onDown: " + event.toString());
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
                Utilities.playPageFlip(BinderActivity.this);
                if (x1 > x2) {
                    // swipe left, next
                    mGridView.setAdapter(new ImageAdapter(BinderActivity.this, mStrArr, mColumnWidth, mColumnHeight));
                } else {
                    // swipe right, prev
                    mGridView.setAdapter(new ImageAdapter(BinderActivity.this, mStrArr, mColumnWidth, mColumnHeight));
                }
            }
            return true;
        }
    }
}
