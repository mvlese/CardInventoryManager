package net.leseonline.cardinventorymanager;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    private TextView mPlayerTextView;
    private TextView mTeamTextView;
    private TextView mValueTextView;
    private TextView mYearTextView;
    private TextView mConditionTextView;
    private TextView mCompanyTextView;
    private GridLayout mSortGridLayout;
    private HashMap<Integer, Integer> mOrderMap;
    private int mDisplayWidth;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mOrderMap = new HashMap<Integer, Integer>();
        int n = 0;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDisplayWidth = metrics.widthPixels ;

        mSortGridLayout = (GridLayout)findViewById(R.id.settings_grid_sort_order);

        mPlayerTextView = (TextView) findViewById(R.id.sort_player_text_view);
        mOrderMap.put(R.id.sort_player_text_view, n++);
        mPlayerTextView.setTag("player-tag");
        mPlayerTextView.setOnDragListener(new DragEventListener());
        mPlayerTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handleOnLongClickForTextView(v, "player");
                return true;
            }
        });

        mTeamTextView = (TextView) findViewById(R.id.sort_team_text_view);
        mOrderMap.put(R.id.sort_team_text_view, n++);
        mTeamTextView.setTag("team-tag");
        mTeamTextView.setOnDragListener(new DragEventListener());
        mTeamTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handleOnLongClickForTextView(v, "team");
                return true;
            }
        });

        mValueTextView = (TextView) findViewById(R.id.sort_value_text_view);
        mOrderMap.put(R.id.sort_value_text_view, n++);
        mValueTextView.setTag("value-tag");
        mValueTextView.setOnDragListener(new DragEventListener());
        mValueTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handleOnLongClickForTextView(v, "value");
                return true;
            }
        });

        mYearTextView = (TextView) findViewById(R.id.sort_year_text_view);
        mOrderMap.put(R.id.sort_year_text_view, n++);
        mYearTextView.setTag("year-tag");
        mYearTextView.setOnDragListener(new DragEventListener());
        mYearTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handleOnLongClickForTextView(v, "year");
                return true;
            }
        });

        mConditionTextView = (TextView) findViewById(R.id.sort_condition_text_view);
        mOrderMap.put(R.id.sort_condition_text_view, n++);
        mConditionTextView.setTag("condition-tag");
        mConditionTextView.setOnDragListener(new DragEventListener());
        mConditionTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handleOnLongClickForTextView(v, "condition");
                return true;
            }
        });

        mCompanyTextView = (TextView) findViewById(R.id.sort_company_text_view);
        mOrderMap.put(R.id.sort_company_text_view, n++);
        mCompanyTextView.setTag("company-tag");
        mCompanyTextView.setOnDragListener(new DragEventListener());
        mCompanyTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handleOnLongClickForTextView(v, "company");
                return true;
            }
        });

//        for (final Integer key: mOrderMap.keySet()) {
//            Integer resource = mOrderMap.get(key);
//            TextView view = (TextView) findViewById(resource);
//            view.setTag(key.toString());
//            view.setOnDragListener(new DragEventListener());
//            view.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    handleOnLongClickForTextView(v, key.toString());
//                    return true;
//                }
//            });
//        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void handleOnLongClickForTextView(View v, String tag) {
        // Create a new ClipData.
        // This is done in two steps to provide clarity. The convenience method
        // ClipData.newPlainText() can create a plain text ClipData in one step.

        // Create a new ClipData.Item from the ImageView object's tag
        ClipData.Item item = new ClipData.Item((String) v.getTag());

        // Create a new ClipData using the tag as a label, the plain text MIME type, and
        // the already-created item. This will create a new ClipDescription object within the
        // ClipData, and set its MIME type entry to "text/plain"
        v.getTag();
        ClipData dragData = ClipData.newPlainText((String) v.getTag(), tag);

        // Instantiates the drag shadow builder.
        View.DragShadowBuilder myShadow = new MyDragShadowBuilder(v);

        // Starts the drag

        v.startDrag(dragData,  // the data to be dragged
                myShadow,  // the drag shadow builder
                null,      // no need to use local data
                0          // flags (not currently used, set to 0)
        );
    }

    private View getSwitchView(View textView) {
        View switchView = null;
        int num = mSortGridLayout.getChildCount();
        boolean done = false;
        int n = 0;
        while (n < num && !done) {
            if (textView == mSortGridLayout.getChildAt(n)) {
                switchView = mSortGridLayout.getChildAt(n + 1);
                done = true;
            } else {
                n += 2;
            }
        }
        return switchView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Settings Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://net.leseonline.cardinventorymanager/http/host/path")
        );
        AppIndex.AppIndexApi.start(mClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Settings Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://net.leseonline.cardinventorymanager/http/host/path")
        );
        AppIndex.AppIndexApi.end(mClient, viewAction);
        mClient.disconnect();
    }

    private class MyDragShadowBuilder extends View.DragShadowBuilder {

        // The drag shadow image, defined as a drawable thing
        private Drawable shadow;
        private int mWidth;
        private int mHeight;
        View mSwitchView = null;

        // Defines the constructor for myDragShadowBuilder
        public MyDragShadowBuilder(View v) {

            // Stores the View parameter passed to myDragShadowBuilder.
            super(v);

            mSwitchView = SettingsActivity.this.getSwitchView(v);

            // Creates a draggable image that will fill the Canvas provided by the system.
            shadow = new ColorDrawable(Color.GRAY);
        }

        // Defines a callback that sends the drag shadow dimensions and touch point back to the
        // system.
        @Override
        public void onProvideShadowMetrics(Point size, Point touch) {
            // Defines local variables
            int width, height;

            // Sets the width of the shadow to half the width of the original View
            width = getView().getWidth();

            // Sets the height of the shadow to half the height of the original View
            height = getView().getHeight();

            // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
            // Canvas that the system will provide. As a result, the drag shadow will fill the
            // Canvas.
            shadow.setBounds(0, -20, SettingsActivity.this.mDisplayWidth, height);

            // Sets the size parameter's width and height values. These get back to the system
            // through the size parameter.
            size.set(SettingsActivity.this.mDisplayWidth, height);

            // Sets the touch point's position to be in the middle of the drag shadow
            touch.set(SettingsActivity.this.mDisplayWidth / 4, height / 2);
        }

        // Defines a callback that draws the drag shadow in a Canvas that the system constructs
        // from the dimensions passed in onProvideShadowMetrics().
        @Override
        public void onDrawShadow(Canvas canvas) {

            // Draws the ColorDrawable in the Canvas passed in from the system.
            shadow.draw(canvas);
        }
    }

    protected class DragEventListener implements View.OnDragListener {
        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
        Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            // Defines a variable to store the action type for the incoming event
            final int action = event.getAction();
            CharSequence dragData;
            View switchView = SettingsActivity.this.getSwitchView(v);

            // Handles each of the expected events
            switch (action) {

                case DragEvent.ACTION_DRAG_STARTED:
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundDrawable(enterShape);
                    switchView.setBackgroundDrawable(enterShape);
                    break;

                case DragEvent.ACTION_DRAG_LOCATION:
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundDrawable(normalShape);
                    switchView.setBackgroundDrawable(normalShape);
                    break;

                case DragEvent.ACTION_DROP:

                    // Gets the item containing the dragged data
                    ClipData.Item item = event.getClipData().getItemAt(0);

                    // Gets the text data from the item.
                    dragData = item.getText();

                    String tag = (String) v.getTag();
                    // Displays a message containing the dragged data.
                    Toast.makeText(v.getContext(), "Dragged data is " + dragData + ", dropped on " + tag, Toast.LENGTH_SHORT).show();

                    // Turns off any color tints
                    //v.clearColorFilter();

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    break;

                case DragEvent.ACTION_DRAG_ENDED:

                    // Turns off any color tinting
                    //v.clearColorFilter();
                    v.setBackgroundDrawable(normalShape);
                    switchView.setBackgroundDrawable(normalShape);
                    // Invalidates the view to force a redraw
                    v.invalidate();
                    switchView.invalidate();

                    // Does a getResult(), and displays what happened.
                    if (event.getResult()) {
                        //Toast.makeText(v.getContext(), "The drop was handled.", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(v.getContext(), "The drop didn't work.", Toast.LENGTH_LONG).show();
                    }

                    break;

                // An unknown action type was received.
                default:
                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                    break;
            }
            return true;
        }
    }
}
