package net.leseonline.cardinventorymanager;

import android.content.Context;
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

public class ImageAdapter extends BaseAdapter {
    private int[] imageIds = new int[] {
            R.drawable.bc1,
            R.drawable.bc2,
            R.drawable.bc3,
            R.drawable.bc4,
            R.drawable.bc5,
            R.drawable.bc6,
            R.drawable.bc7,
            R.drawable.bc8,
            R.drawable.bc9,
    };
    private Context context;
    private final String[] mobileValues;
    int column_width , column_height ;
    public ImageAdapter(Context context, String[] arrValues , int column_width ,int column_height) {
        this.context = context;
        this.mobileValues = arrValues;
        this.column_width = column_width ;
        this.column_height = column_height ;
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
            Drawable drawable = context.getResources().getDrawable(imageIds[position]);
            viewHolder.imageView.setImageDrawable(drawable);
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