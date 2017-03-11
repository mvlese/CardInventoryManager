package net.leseonline.cardinventorymanager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.leseonline.cardinventorymanager.db.DatabaseHelper;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Vibrator myVib;
    private ImageButton singleViewButton;
    private ImageButton binderViewButton;
    private ImageButton cameraViewButton;
    private ImageButton settingsViewButton;
    private DatabaseHelper mDatabaseHelper;
    private int mCardUniqueId;
    private final int TAKE_PICTURE_REQUEST = 7;
    private final int CAPTURE_DATA_REQUEST = 6;
    private final String TAG = "MainActivity";
    private File mPhotoFile;

    private enum CameraState {
        CAPTURE_IDLE,
        CAPTURE_FRONT,
        CAPTURE_BACK,
        CAPTURE_DATA,
        CAPTURE_COMPLETE
    }
    private CameraState mCameraState = CameraState.CAPTURE_IDLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseHelper = new DatabaseHelper(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCardUniqueId = -1;

        File filesDir = this.getFilesDir();
        mPhotoFile = new File(filesDir, "image.jpg");

        // TODO mvl - Delete this and the "next uniqueId" code when necessary.
        deleteDatabase(DatabaseHelper.DATABASE_NAME);
        int n = mDatabaseHelper.getNextUniqueid();
        Log.d(TAG, String.valueOf(n));
        n = mDatabaseHelper.getNextUniqueid();
        Log.d(TAG, String.valueOf(n));
        n = mDatabaseHelper.getNextUniqueid();
        Log.d(TAG, String.valueOf(n));

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDisplayWidth = metrics.widthPixels ;
        int usableWidth = (int)(mDisplayWidth * 0.85);

        singleViewButton = (ImageButton)findViewById(R.id.card_view);
        singleViewButton.setLayoutParams(getLayoutParams(usableWidth));
        singleViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SingleCardActivity.class);
                startActivity(intent);
            }
        });

        binderViewButton = (ImageButton)findViewById(R.id.binder_view);
        binderViewButton.setLayoutParams(getLayoutParams(usableWidth));
        binderViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BinderActivity.class);
                startActivity(intent);
            }
        });

        cameraViewButton = (ImageButton)findViewById(R.id.camera_view);
        cameraViewButton.setLayoutParams(getLayoutParams(usableWidth));
        cameraViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Toast.makeText(MainActivity.this.getApplicationContext(), "Capture an Image", Toast.LENGTH_SHORT).show();
//                Intent captureIntent = new Intent(MainActivity.this.getApplicationContext(), CaptureActivity.class);
//                startActivity(captureIntent);
                final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                boolean canTakePhoto = (captureImage.resolveActivity(getPackageManager()) != null);
                if (canTakePhoto) {
                    // This uniqueId will be used to form the image file names.
                    mCardUniqueId = mDatabaseHelper.getNextUniqueid();
                    mCameraState = CameraState.CAPTURE_FRONT;
                    captureImage(captureImage, mCardUniqueId, true);
                }
            }
        });

        settingsViewButton = (ImageButton)findViewById(R.id.settings_view);
        settingsViewButton.setLayoutParams(getLayoutParams(usableWidth));
        settingsViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);

                // TODO mvl - This does not belong here.
//                Intent intent = new Intent(MainActivity.this, CaptureDataActivity.class);
//                intent.putExtra(getResources().getString(R.string.extra_unique_id), mCardUniqueId);
//                startActivityForResult(intent, CAPTURE_DATA_REQUEST);
            }
        });

//        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setHapticFeedbackEnabled(true);
//        fab.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                myVib.vibrate(500);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
//            }
//        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Save", Toast.LENGTH_SHORT).show();
                // Do something
                // If CAPTURE_FRONT, then save front image, change state to CAPTURE_BACK
                // If CAPTURE_BACK, then save back image, change state to CAPTURE_DATA
                switch (mCameraState) {
                    case CAPTURE_IDLE:
                        break;
                    case CAPTURE_FRONT:
                        revokeWriteUriPermission();
                        mCameraState = CameraState.CAPTURE_BACK;
                        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        captureImage(captureImage, mCardUniqueId, false);
                        break;
                    case CAPTURE_BACK:
                        mCameraState = CameraState.CAPTURE_DATA;
                        revokeWriteUriPermission();
                        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getPath());
                        int width = bitmap.getWidth();
                        Intent intent = new Intent(MainActivity.this, CaptureDataActivity.class);
                        intent.putExtra(getResources().getString(R.string.extra_unique_id), mCardUniqueId);
                        startActivityForResult(intent, CAPTURE_DATA_REQUEST);
                        break;
                    case CAPTURE_DATA:
                        mCameraState = CameraState.CAPTURE_IDLE;
                        break;
                    case CAPTURE_COMPLETE:
                        break;
                    default:
                        break;
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                // Do something else
                // If CAPTURE_FRONT, then change state to CAPTURE_IDLE.
                // If CAPTURE_BACK, then prompt to keep front.  If keep front, save front, CHANGE_STATE to CAPTURE_DATA.
                switch (mCameraState) {
                    case CAPTURE_IDLE:
                        break;
                    case CAPTURE_FRONT:
                        revokeWriteUriPermission();
                        mCameraState = CameraState.CAPTURE_IDLE;
                        break;
                    case CAPTURE_BACK:
                        revokeWriteUriPermission();
                        mCameraState = CameraState.CAPTURE_IDLE;
                        break;
                    case CAPTURE_DATA:
                        mCameraState = CameraState.CAPTURE_IDLE;
                        break;
                    case CAPTURE_COMPLETE:
                        break;
                    default:
                        break;
                }
            }
        } else if (requestCode == CAPTURE_DATA_REQUEST) {
            if (resultCode == RESULT_OK) {
                BaseballCard card = CaptureDataActivity.getCard();
                Log.d(TAG, card.toString());
            } else if (resultCode == RESULT_CANCELED) {

            }
            mCameraState = CameraState.CAPTURE_IDLE;
        }
    }

    private void captureImage(Intent captureImage, int uniqueId, boolean isFront) {
        File filesDir = MainActivity.this.getFilesDir();
        String pre = isFront ? "IMGF_" : "IMGB_";
        mPhotoFile = new File(filesDir, pre + String.valueOf(mCardUniqueId) + ".jpg");

        Uri uri = FileProvider.getUriForFile(this,
                "net.leseonline.fileprovider",
                MainActivity.this.mPhotoFile);
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        List<ResolveInfo> cameraActivities = this
                .getPackageManager().queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity : cameraActivities) {
            this.grantUriPermission(activity.activityInfo.packageName,
                    uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        startActivityForResult(captureImage, TAKE_PICTURE_REQUEST);
    }

    private void revokeWriteUriPermission() {
        Uri uri = FileProvider.getUriForFile(this,
                "net.leseonline.fileprovider",
                mPhotoFile);

        this.revokeUriPermission(uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }
    private void captureData() {
        // show edit activity with CAPTURE_DATA_REQUEST
    }

    private GridLayout.LayoutParams getLayoutParams(int size) {
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.height = size / 2;
        layoutParams.width = size / 2;
        return layoutParams;
    }
}
