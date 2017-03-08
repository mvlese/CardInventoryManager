package net.leseonline.cardinventorymanager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class BinderViewActivity extends AppCompatActivity {
    private int mDisplayWidth;
    private int mDisplayHeight;
    private int mStatusBarHeight;
    private int mRequiredHeight;
    private GridView mGridView;

    private static int Width = 586;
    private static int Height = 784;
    private static int LeftOffset = 130;
    private static int TopOffset = 30;
    private static int RightTickMark = 558;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_binder_view);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

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
        String[] str_arr = new String[arrSize];
        for(int i = 0; i < arrSize; i++){
            str_arr[i] = String.valueOf(i);
        }
        int column_width = mDisplayWidth / column ;
        int column_height = mRequiredHeight / column ;
        mGridView.setAdapter(new ImageAdapter(this, str_arr,column_width,column_height));
        enableDisableView(mGridView, false);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        String.valueOf(position), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
}
