package com.example.gaminglibrary.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends CursorAdapter {
    LayoutInflater meinLayoutInflater;
    int itemLayout;
    String[] from;
    int[] to;

    public MyAdapter(Context ctx, int itemLayout, Cursor c, String[] from, int[] to, int flags) {
        super(ctx, c, flags);
        meinLayoutInflater = LayoutInflater.from(ctx);
        this.itemLayout = itemLayout;
        this.from = from;
        this.to = to;
    }

    @Override
    public View newView(Context ctx, Cursor c, ViewGroup parent) {
        View v = meinLayoutInflater.inflate(itemLayout, parent, false);
        return v;
    }

    @Override
    public void bindView(View v, Context ctx, Cursor c) {
        int image = c.getInt(c.getColumnIndexOrThrow(from[0]));
        ImageView imageView = (ImageView) v.findViewById(to[0]);
        imageView.setImageResource(image);
        String text1 = c.getString(c.getColumnIndexOrThrow(from[1]));
        TextView textView1 = (TextView) v.findViewById(to[1]);
        textView1.setText(text1);
        String text2 = c.getString(c.getColumnIndexOrThrow(from[2]));
        TextView textView2 = (TextView) v.findViewById(to[2]);
        textView2.setText(text2);
    }

}

