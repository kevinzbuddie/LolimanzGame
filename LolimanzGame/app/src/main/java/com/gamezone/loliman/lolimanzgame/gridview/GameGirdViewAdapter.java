package com.gamezone.loliman.lolimanzgame.gridview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamezone.loliman.lolimanzgame.R;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Loliman on 2018/1/10.
 */

public class GameGirdViewAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private int mCount=0;
    private int mColumn=0;
    private int mViewHeight=0;
    private HashMap mGameMap = new HashMap();
    private int mGameMapArray[];

    private int clickTemp = -1;
    private int firstClick = -1;

    public GameGirdViewAdapter(Context context, int column, int viewHeight, SerializableHashMap map) {
        mCount = column * column;
        mColumn = column;
        mViewHeight = viewHeight;
        mGameMap = map.getMap();
        mLayoutInflater = LayoutInflater.from(context);

        mGameMapArray = new int[mCount];
        HashSet integerHashSet=new HashSet();
        Random random = new Random();//创建随机对象

        for (int i = 0; i < mCount; i++) {
            int randomInt=random.nextInt(mGameMap.size());
            if (!integerHashSet.contains(randomInt)) {
                integerHashSet.add(randomInt);
            }else{
                i--;
            }
        }

        int i=0,j=0;
        Iterator it;
        for(it = integerHashSet.iterator(); it.hasNext();) {
            mGameMapArray[i] = (int)it.next()+1; //plus 1 to avoid 0 index in the game map xml
            i++;
        }
        while ( i == j || j == 0 ) {
            i = random.nextInt(mCount);
            j = random.nextInt(mCount);
        }

        mGameMapArray[i] = mGameMapArray[j]; //make the game point, two same item index!
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
        String ResString;
        int ResId;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.layout_grid_item, null);

            vh = new ViewHolder();
            vh.imageView = convertView.findViewById(R.id.id_gird_item_image_001);

            ResString = mGameMap.get(String.valueOf(mGameMapArray[position])).toString();
            ResId = getResId(ResString, R.drawable.class);
            vh.imageView.setImageResource(ResId);

            vh.textView = convertView.findViewById(R.id.id_grid_item_text_002);
            vh.textView.setText(String.valueOf(mGameMapArray[position]));

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)vh.imageView.getLayoutParams();
            layoutParams.height = (mViewHeight-20)/mColumn; //20 is two times of padding size of gridView.
            vh.imageView.setLayoutParams(layoutParams);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();

            if (clickTemp == position) {
                vh.imageView.setBackgroundResource(R.drawable.png_mask);
            }

        }
        return convertView;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
