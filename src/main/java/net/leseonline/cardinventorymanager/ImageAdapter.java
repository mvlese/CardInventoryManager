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

import java.util.ArrayList;
import java.util.Random;

public class ImageAdapter extends BaseAdapter {
    private ArrayList<Integer> mIndices;
    private int mCurrentIndex;
    private Bitmap[] mBitmaps;
    private Context context;
    private final String[] mobileValues;
    int column_width , column_height ;
    public ImageAdapter(Context context, String[] arrValues , int column_width ,int column_height) {
        this.context = context;
        this.mobileValues = arrValues;
        this.column_width = column_width ;
        this.column_height = column_height ;

        Random random = new Random(System.currentTimeMillis());

        mIndices = new ArrayList<>();
        mCurrentIndex = 0;

        mBitmaps = new Bitmap[] {
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc1),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc2),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc3),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc4),
                // BitmapFactory.decodeResource(context.getResources(), R.drawable.bc5),
                // BitmapFactory.decodeResource(context.getResources(), R.drawable.bc6),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc7),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc8),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc9),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc10),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bc11),
        };

        while (mIndices.size() != mBitmaps.length) {
            int index = random.nextInt(mBitmaps.length);
            // int index = ((int)(Math.random() * 10000)) % mBitmaps.length;
            if (!mIndices.contains(index)) {
                mIndices.add(index);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder ;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_item, null);
            // set image based on selected text
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
//            Drawable drawable = context.getResources().getDrawable(imageIds[position]);
            viewHolder.imageView.setImageBitmap(mBitmaps[mIndices.get(position)]);
//            viewHolder.imageView.setImageDrawable(drawable);
            viewHolder.lin = (LinearLayout)convertView.findViewById(R.id.lin);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        String mobile = mobileValues[position];
        if(position%2 == 1){
      //      viewHolder.lin.setBackgroundColor(Color.RED);
        }else{
        //    viewHolder.lin.setBackgroundColor(Color.GREEN);
        }
        android.widget.AbsListView.LayoutParams parms = new android.widget.AbsListView.LayoutParams(column_width, column_height);
        viewHolder.lin.setLayoutParams(parms);
        return convertView;
    }

    @Override
    public int getCount() {
        return mobileValues.length;
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

}