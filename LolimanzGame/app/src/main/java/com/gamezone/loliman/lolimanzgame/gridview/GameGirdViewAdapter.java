package com.gamezone.loliman.lolimanzgame.gridview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gamezone.loliman.lolimanzgame.R;

/**
 * Created by Loliman on 2018/1/10.
 */

public class GameGirdViewAdapter extends BaseAdapter {

    private Context mContext=null;
    private LayoutInflater mLayoutInflater;
    private int mCount=0;

    public GameGirdViewAdapter(Context context, int count) {
        mCount = count;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder{
        public ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.layout_grid_item, null);

            vh = new ViewHolder();
            vh.imageView = convertView.findViewById(R.id.id_gird_item_image_001);
            vh.imageView.setImageResource(R.drawable.png_0361);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        return convertView;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
