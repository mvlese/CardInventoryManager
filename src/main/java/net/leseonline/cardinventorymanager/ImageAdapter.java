package net.leseonline.cardinventorymanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.leseonline.cardinventorymanager.db.DatabaseHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ImageAdapter extends ArrayAdapter<CardItem> {
//    private ArrayList<Integer> mIndices;
//    private int mCurrentIndex;
//    private Bitmap[] mBitmaps;
    private Context mContext;
    private int mResourceId;
    private ArrayList<CardItem> mData;
    //private final Long[] mIds;
    private int mColumnWidth;
    private int mColumnHeight;
    private DatabaseHelper mDatabaseHelper;
    private final String TAG = "ImageAdapter";

    public ImageAdapter(Context context, int resourceId, ArrayList<CardItem> data, int columnWidth, int columnHeight) {
//    public ImageAdapter(Context context, Long[] arrValues, int column_width, int column_height) {
        super(context, resourceId, data);
        this.mContext = context;
        this.mResourceId = resourceId;
        this.mData = data;
//        this.mIds = arrValues;
        this.mColumnWidth = columnWidth;
        this.mColumnHeight = columnHeight;
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
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        ViewHolder viewHolder ;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
            // set image based on selected text
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
//            BaseballCard card = mDatabaseHelper.find(mIds[position]);
//            int uniqueId = card.getUniqueId();
//            setImage(viewHolder.imageView, uniqueId);

            viewHolder.lin = (LinearLayout)convertView.findViewById(R.id.lin);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        CardItem item = mData.get(position);
        viewHolder.imageView.setImageBitmap(item.getFrontImage());
        android.widget.AbsListView.LayoutParams parms = new android.widget.AbsListView.LayoutParams(mColumnWidth, mColumnHeight);
        viewHolder.lin.setLayoutParams(parms);
        return convertView;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CardItem getItem(int position) {
        return mData.get(position);
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

}