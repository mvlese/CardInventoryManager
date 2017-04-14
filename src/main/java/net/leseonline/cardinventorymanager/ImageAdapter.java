package net.leseonline.cardinventorymanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.leseonline.cardinventorymanager.db.DatabaseHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class ImageAdapter extends BaseAdapter {
//    private ArrayList<Integer> mIndices;
//    private int mCurrentIndex;
//    private Bitmap[] mBitmaps;
    private Context mContext;
    private final long[] mIds;
    private int mColumnWidth;
    private int mColumnHeight;
    private DatabaseHelper mDatabaseHelper;
    private final int CARD_TAG = 7;

    public ImageAdapter(Context context, long[] arrValues, int column_width, int column_height) {
        this.mContext = context;
        this.mIds = arrValues;
        this.mColumnWidth = column_width;
        this.mColumnHeight = column_height;
        mDatabaseHelper = new DatabaseHelper(context);

//        Random random = new Random(System.currentTimeMillis());
//
//        mIndices = new ArrayList<>();
//        mCurrentIndex = 0;
//
//        mBitmaps = new Bitmap[]{
//                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc1),
//                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc2),
//                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc3),
//                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc4),
//                // BitmapFactory.decodeResource(context.getResources(), R.drawable.bc5),
//                // BitmapFactory.decodeResource(context.getResources(), R.drawable.bc6),
//                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc7),
//                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc8),
//                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc9),
//                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc10),
//                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc11),
//        };
//
//        while (mIndices.size() != mBitmaps.length) {
//            int index = random.nextInt(mBitmaps.length);
//            // int index = ((int)(Math.random() * 10000)) % mBitmaps.length;
//            if (!mIndices.contains(index)) {
//                mIndices.add(index);
//            }
//        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder ;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_item, null);
            // set image based on selected text
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseballCard card = (BaseballCard)v.getTag(CARD_TAG);
                    Toast.makeText(mContext, "Image Clicked: " + card.getFullName(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseballCard card = mDatabaseHelper.find(mIds[position]);
            int uniqueId = card.getUniqueId();
            setImage(viewHolder.imageView, uniqueId);
            viewHolder.imageView.setTag(CARD_TAG, card);

            viewHolder.lin = (LinearLayout)convertView.findViewById(R.id.lin);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        android.widget.AbsListView.LayoutParams parms = new android.widget.AbsListView.LayoutParams(mColumnWidth, mColumnHeight);
        viewHolder.lin.setLayoutParams(parms);
        return convertView;
    }

    @Override
    public int getCount() {
        return mIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     *
     */
    public class ViewHolder {
        ImageView imageView ;
        LinearLayout lin ;
    }

    private void setImage(ImageView iv, int cardId) {
        File filesDir = mContext.getFilesDir();
        String pre = "IMGF_";
        File file = new File(filesDir, pre + String.valueOf(Math.abs(cardId)) + ".jpg");
        iv.setImageDrawable(Drawable.createFromPath(file.getPath()));
    }

}