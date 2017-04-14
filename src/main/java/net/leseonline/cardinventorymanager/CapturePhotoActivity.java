package net.leseonline.cardinventorymanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CapturePhotoActivity extends AppCompatActivity {
    private final String TAG = "CapturePhotoActivity";
    private Camera mCamera;
    private CameraPreview mPreview;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public int mUniqueCardId;
    public byte[] mData;
    public Bitmap mBitmapToWrite;
    private CaptureState mState;
    private CaptureEvent mEvent;
    private Button mCaptureButton;
    private Button mCancelButton;
    private Button mConfirmButton;

    enum CaptureEvent {
        OnCancel,
        OnCapture,
        OnRedo,
        OnConfirm
    }

    enum CaptureState {
        CS_CAPTURE_FRONT,
        CS_FRONT_FROZEN,
        CS_CAPTURE_BACK,
        CS_BACK_FROZEN,
        CS_END
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_photo);

        // Create an instance of Camera
        mCamera = getCameraInstance();
        mData = new byte[0];

        mState = CaptureState.CS_CAPTURE_FRONT;
        mEvent = CaptureEvent.OnCancel;

        // If MIN_VALUE, it is bad.  If it is negative, it is the back, else the front.
        mUniqueCardId = getIntent().getIntExtra(getResources().getString(R.string.extra_unique_id), Integer.MIN_VALUE);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        mCamera.setDisplayOrientation(90);
        Camera.Parameters p = mCamera.getParameters();
        p.setPictureSize(320, 240);
        mCamera.setParameters(p);
        final FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        ViewGroup.LayoutParams params = preview.getLayoutParams();
        params.width = 480;
        params.height = 640;
        preview.setLayoutParams(params);

        // Add a listener to the Capture button
        mCaptureButton = (Button) findViewById(R.id.button_capture);
        mCaptureButton.setText("Take Front Picture");
        mCaptureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleEvent(CaptureEvent.OnCapture);
                    }
                }
        );

        mConfirmButton = (Button) findViewById(R.id.button_OK);
        mConfirmButton.setText("Save Front");
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEvent(CaptureEvent.OnConfirm);
            }
        });
        mConfirmButton.setEnabled(false);

        mCancelButton = (Button) findViewById(R.id.button_cancel);
        mCancelButton.setText("Cancel");
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get an image from the camera
                handleEvent(CaptureEvent.OnCancel);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private boolean saveData(boolean isFront) {
        boolean result = false;
        if (mBitmapToWrite != null) {
            File filesDir = this.getFilesDir();
            String pre = isFront ? "IMGF_" : "IMGB_";
            File pictureFile = new File(filesDir, pre + String.valueOf(Math.abs(mUniqueCardId)) + ".jpg");
            if (pictureFile == null) {
            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    mBitmapToWrite.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                    // fos.write(mData);
                    fos.close();
                    result = true;
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
            }
        }
        return result;
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            mBitmapToWrite = null;
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;
            int w = bm.getWidth();
            int h = bm.getHeight();
            // Notice that width and height are reversed
//            Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
//            w = scaled.getWidth();
//            h = scaled.getHeight();
            // Setting post rotate to 90
            Matrix mtx = new Matrix();
            mtx.postRotate(90);
            // Rotating Bitmap
            mBitmapToWrite = Bitmap.createBitmap(bm, 0, 0, w, h, mtx, true);

            mCamera.stopPreview();
//            mData = data;
        }
    };

    private File getOutputMediaFile(boolean isFront) {
        File filesDir = this.getFilesDir();
        String pre = isFront ? "IMGF_" : "IMGB_";
        File photoFile = new File(filesDir, pre + String.valueOf(mUniqueCardId) + ".jpg");
        return photoFile;
    }

    private void completeCapture() {
        setResult(RESULT_OK);
        finish();
    }

    private void cancelThis() {
        mCamera.startPreview();
        mData = new byte[0];
        setResult(RESULT_CANCELED);
        finish();
    }

    private void handleEvent(CaptureEvent event) {
        switch(mState) {
            case CS_CAPTURE_FRONT:
                switch(event) {
                    case OnCancel:
                        cancelThis();
                        break;
                    case OnCapture:
                        mCamera.takePicture(null, null, mPicture);
                        mConfirmButton.setEnabled(true);
                        mCaptureButton.setText("Redo Front");
                        mState = CaptureState.CS_FRONT_FROZEN;
                        break;
                    case OnConfirm:
                        break;
                    case OnRedo:
                        break;
                    default:
                        break;
                }
                break;
            case CS_FRONT_FROZEN:
                switch(event) {
                    case OnCancel:
                        cancelThis();
                        break;
                    case OnCapture:
                        break;
                    case OnConfirm:
                        if (saveData(true)) {
                            mCamera.startPreview();
                            mConfirmButton.setEnabled(false);
                            mCaptureButton.setText("Take Back Picture");
                            mCancelButton.setText("Skip Back");
                            mConfirmButton.setText("Save Back");
                            mState = CaptureState.CS_CAPTURE_BACK;
                        }
                        break;
                    case OnRedo:
                        mCamera.startPreview();
                        mConfirmButton.setEnabled(false);
                        mCaptureButton.setText("Take Front Picture");
                        mCancelButton.setText("Cancel");
                        mConfirmButton.setText("Save Front");
                        mState = CaptureState.CS_CAPTURE_FRONT;
                        break;
                    default:
                        break;
                }
                break;
            case CS_CAPTURE_BACK:
                switch(event) {
                    case OnCancel:
                        completeCapture();
                        break;
                    case OnCapture:
                        mCamera.takePicture(null, null, mPicture);
                        mConfirmButton.setEnabled(true);
                        mCaptureButton.setText("Redo Back");
                        mState = CaptureState.CS_BACK_FROZEN;
                        break;
                    case OnConfirm:
                        break;
                    case OnRedo:
                        break;
                    default:
                        break;
                }
                break;
            case CS_BACK_FROZEN:
                switch(event) {
                    case OnCancel:
                        completeCapture();
                        break;
                    case OnCapture:
                        break;
                    case OnConfirm:
                        if (saveData(false)) {
                            mCamera.startPreview();
                            mConfirmButton.setEnabled(false);
                            mCaptureButton.setText("Take Front Picture");
                            mCancelButton.setText("Cancel");
                            mConfirmButton.setText("Save Front");
                            mState = CaptureState.CS_CAPTURE_FRONT;
                            completeCapture();
                        }
                        break;
                    case OnRedo:
                        mCamera.startPreview();
                        mConfirmButton.setEnabled(false);
                        mCaptureButton.setText("Take Back Picture");
                        mCancelButton.setText("Skip Back");
                        mConfirmButton.setText("Save Back");
                        mState = CaptureState.CS_CAPTURE_BACK;
                        break;
                    default:
                        break;
                }
                break;
            case CS_END:
                switch(event) {
                    case OnCancel:
                        break;
                    case OnCapture:
                        break;
                    case OnConfirm:
                        break;
                    case OnRedo:
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}
