package com.gamezone.loliman.lolimanzgame.gridview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamezone.loliman.lolimanzgame.R;

import java.lang.reflect.Field;

/**
 * Created by Loliman on 2018/1/12.
 */

public class GameSetEntriesAdapter extends BaseAdapter {

    private Context mContext=null;
    private LayoutInflater mLayoutInflater;
    private int mColumn=0;
    private int mViewHeight;
    private int mGameSetData[][];

    private int clickTemp = -1;

    public GameSetEntriesAdapter(Context context, int column, int view_height, int [][] set_data) {
        mColumn =  column;
        mViewHeight = view_height;
        mGameSetData = set_data;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mColumn*mColumn;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }

    static class ViewHolder{
        public ImageView imageView;
        public TextView textView;
    }

    //用反射机制通过字符串获取图片资源id的方法！
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        int ResId;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.layout_grid_item, null);

            vh = new ViewHolder();
            vh.imageView = convertView.findViewById(R.id.id_gird_item_image_001);
            vh.imageView.setPadding(20,20,20,20);
            vh.imageView.setImageResource(R.drawable.png_1160);

            if (position >0 && mGameSetData[position][0] == 1){
                vh.imageView.setImageResource(R.drawable.png_lock);
            }

            vh.textView = convertView.findViewById(R.id.id_grid_item_text_002);
            vh.textView.setPadding(35,35,35,35);
            vh.textView.setVisibility(View.VISIBLE);
            vh.textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            vh.textView.setGravity(Gravity.BOTTOM);
            vh.textView.setTextSize(26);
            vh.textView.setText(String.valueOf(position+1));

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)vh.imageView.getLayoutParams();
            layoutParams.height = (mViewHeight-20)/mColumn; //20 is two times of padding size of gridView.
            vh.imageView.setLayoutParams(layoutParams);
            convertView.setTag(vh);

        }else{
            vh = (ViewHolder)convertView.getTag();

        }
        return convertView;    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
