package com.example.gaminglibrary.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyAdapter extends CursorAdapter {
    LayoutInflater myLayoutInflater;
    int itemLayout;
    String[] from;
    int[] to;
    private int STORAGE_PERMISSION_CODE = 1;

    public MyAdapter(Context ctx, int itemLayout, Cursor c, String[] from, int[] to, int flags) {
        super(ctx, c, flags);
        myLayoutInflater = LayoutInflater.from(ctx);
        this.itemLayout = itemLayout;
        this.from = from;
        this.to = to;
    }

    @Override
    public View newView(Context ctx, Cursor c, ViewGroup parent) {
        View v = myLayoutInflater.inflate(itemLayout, parent, false);
        return v;
    }

    @Override
    public void bindView(View v, Context ctx, Cursor c) {
        //try {
            /*Uri myUri = Uri.parse(c.getString(c.getColumnIndexOrThrow(from[0])));
            ImageView imageView = (ImageView) v.findViewById(to[0]);
            Bitmap imageToStore = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), myUri);
            imageView.setImageBitmap(imageToStore);*/
        String text1 = c.getString(c.getColumnIndexOrThrow(from[0]));
        TextView textView1 = (TextView) v.findViewById(to[0]);
        textView1.setText(text1);
        String text2 = c.getString(c.getColumnIndexOrThrow(from[1]));
        TextView textView2 = (TextView) v.findViewById(to[1]);
        textView2.setText(text2 + "€");
        String text3 = c.getString(c.getColumnIndexOrThrow(from[2]));
        TextView textView3 = (TextView) v.findViewById(to[2]);
        textView3.setText(" " + text3 + " ★");

        /*} catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}

