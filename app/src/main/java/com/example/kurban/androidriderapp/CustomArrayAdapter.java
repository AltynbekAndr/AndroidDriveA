package com.example.kurban.androidriderapp;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class CustomArrayAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private List<String> list;
    Context context;
    public CustomArrayAdapter(Context context,List list) {
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View v = convertView;
        if ( v == null){
            holder = new ViewHolder();
            v = inflater.inflate(R.layout.list_item_to_history, parent, false);
            holder.txt1 = (TextView) v.findViewById(R.id.textView);
            //holder.imageView = (ImageView) v.findViewById(R.id.imageView);
            //holder.coordinatorLayout = (LinearLayout) v.findViewById(R.id.coordinator);
            //holder.imageView2 = (ImageView) v.findViewById(R.id.image_view);


            v.setTag(holder);
        }
        holder = (ViewHolder) v.getTag();
        holder.txt1.setText(list.get(position));
        return v;

    }
    private static class ViewHolder {
        private TextView txt1;
        //private ImageView imageView;
        //private ImageView imageView2;
        //private LinearLayout coordinatorLayout;

    }
    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }
    public int color(@ColorRes int resId) {
        return context.getResources().getColor(resId);
    }
}