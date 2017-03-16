package net.leseonline.cardinventorymanager;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;
import android.widget.Toast;

import net.leseonline.cardinventorymanager.db.DatabaseHelper;

import java.io.File;
import java.io.FileFilter;
import java.text.NumberFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  AdminPwDialogFragment.IAdminPwDialogListener {
    private Vibrator myVib;
    private ImageButton singleViewButton;
    private ImageButton binderViewButton;
    private ImageButton cameraViewButton;
    private ImageButton settingsViewButton;
    private DatabaseHelper mDatabaseHelper;
    private int mUniqueCardId;
    private final static int TAKE_PICTURE_REQUEST = 7;
    private final static int CAPTURE_DATA_REQUEST = 6;
    private final static String TAG = "MainActivity";
    private File mPhotoFile;
    NumberFormat mMoneyFormatter = NumberFormat.getCurrencyInstance();

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
        mUniqueCardId = -1;
        mMoneyFormatter = NumberFormat.getCurrencyInstance();

        File filesDir = this.getFilesDir();
        mPhotoFile = new File(filesDir, "image.jpg");

        // TODO mvl - Delete this and the "next uniqueId" code when necessary.
//        deleteDatabase(DatabaseHelper.DATABASE_NAME);
//        int n = mDatabaseHelper.getNextUniqueid();
//        Log.d(TAG, String.valueOf(n));
//        n = mDatabaseHelper.getNextUniqueid();
//        Log.d(TAG, String.valueOf(n));
//        n = mDatabaseHelper.getNextUniqueid();
//        Log.d(TAG, String.valueOf(n));

        setValueText();

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
                // This uniqueId will be used to form the image file names.
                mUniqueCardId = mDatabaseHelper.getNextUniqueid();
                if (mUniqueCardId == -1) {
                    Toast.makeText(MainActivity.this.getApplicationContext(), "Failed to get unique id.", Toast.LENGTH_SHORT).show();
                } else {
                    mCameraState = CameraState.CAPTURE_FRONT;
                    captureImage(true);
//                    Intent captureIntent = new Intent(MainActivity.this.getApplicationContext(), CaptureActivity.class);
                    //captureImage(captureIntent, mCardUniqueId, true);
//                final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                boolean canTakePhoto = (captureImage.resolveActivity(getPackageManager()) != null);
//                if (canTakePhoto) {
//                    // This uniqueId will be used to form the image file names.
//                    mCardUniqueId = mDatabaseHelper.getNextUniqueid();
//                    if (mCardUniqueId == -1) {
//                        // TODO mvl - Handle this error.
//                    } else {
//                        mCameraState = CameraState.CAPTURE_FRONT;
//                        captureImage(captureImage, mCardUniqueId, true);
//                    }
//                }
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
        if (id == R.id.action_admin) {
            // show password dialog
            // if entered password equals admin password, then delete DB and image files.
//            Toast.makeText(MainActivity.this.getApplicationContext(), "Logon to Destroy Database", Toast.LENGTH_SHORT).show();
            FragmentManager fm = getFragmentManager();
            AdminPwDialogFragment dialogFragment = new AdminPwDialogFragment();
            dialogFragment.show(fm, "EnterAdminPw");

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
                        //final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //captureImage(captureImage, mCardUniqueId, false);
                        captureImage(false);
                        break;
                    case CAPTURE_BACK:
                        mCameraState = CameraState.CAPTURE_DATA;
                        revokeWriteUriPermission();
                        Intent intent = new Intent(MainActivity.this, CaptureDataActivity.class);
                        intent.putExtra(getResources().getString(R.string.extra_unique_id), mUniqueCardId);
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
                Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
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
                mDatabaseHelper.addCard(card);
                setValueText();
            } else if (resultCode == RESULT_CANCELED) {

            }
            mCameraState = CameraState.CAPTURE_IDLE;
        }
    }

    @Override
    public void onAdminPwDialogPositiveAction(AdminPwDialogFragment dialog) {
        String mine = getResources().getString(R.string.mine);
        int result = mine.compareTo(dialog.getPw());
        String message = "";
        if (result == 0) {
            try {
                // TODO mvl - destroy DB here and the image files.
                deleteDatabase(DatabaseHelper.DATABASE_NAME);
                deleteAllImageFiles();
                message = "Destroyed DB and deleted files.";
            } catch(Exception ex) {
                message = "Failed to detroy DB and/or delete files.";
            }
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            setValueText();
        }
    }

    @Override
    public void onAdminPwDialogNegativeAction(AdminPwDialogFragment dialog) {

    }

    private void setValueText() {
        float collectionValue = mDatabaseHelper.getCollectionValue();
        String sValue = mMoneyFormatter.format(collectionValue);
        ((TextView)findViewById(R.id.text_value_main)).setText(sValue);
    }

    private void captureImage(boolean isFront) {
//    private void captureImage(Intent captureImage, int uniqueId, boolean isFront) {
//        File filesDir = this.getFilesDir();
//        String pre = isFront ? "IMGF_" : "IMGB_";
//        mPhotoFile = new File(filesDir, pre + String.valueOf(mCardUniqueId) + ".jpg");
//
//        Uri uri = FileProvider.getUriForFile(this,
//                "net.leseonline.fileprovider",
//                MainActivity.this.mPhotoFile);
//        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//
//        List<ResolveInfo> cameraActivities = this
//                .getPackageManager().queryIntentActivities(captureImage,
//                        PackageManager.MATCH_DEFAULT_ONLY);
//
//        for (ResolveInfo activity : cameraActivities) {
//            this.grantUriPermission(activity.activityInfo.packageName,
//                    uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        }
//        startActivityForResult(captureImage, TAKE_PICTURE_REQUEST);

        Intent intent = new Intent(getApplicationContext(), AndroidCameraApi.class);
        int extraData = isFront ? mUniqueCardId : -mUniqueCardId;
        intent.putExtra(getResources().getString(R.string.extra_unique_id), extraData);
        startActivityForResult(intent, TAKE_PICTURE_REQUEST);
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

    private void deleteAllImageFiles() {
        File filesDir = this.getFilesDir();
        if (filesDir.isDirectory())
        {
            String[] children = filesDir.list();
            for (int i = 0; i < children.length; i++)
            {
                if(children[i].toLowerCase().contains(".jpg")) {
                    new File(filesDir, children[i]).delete();
                }
            }
        }
    }

    private File getImageFile() {
        File filesDir = this.getFilesDir();
        String pre = (mUniqueCardId > 0) ? "IMGF_" : "IMGB_";
        File photoFile = new File(filesDir, pre + String.valueOf(Math.abs(mUniqueCardId)) + ".jpg");
        return photoFile;
    }

}
