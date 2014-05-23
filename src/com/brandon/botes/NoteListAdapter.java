package com.brandon.botes;

import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteListAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    public NoteListAdapter(Context context, Cursor c) {
        super(context, c);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context); 
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.notes_row, parent, false);
        return v;
    }

    /**
     * @author will
     * 
     * @param   v
     *          The view in which the elements we set up here will be displayed.
     * 
     * @param   context
     *          The running context where this ListView adapter will be active.
     * 
     * @param   c
     *          The Cursor containing the query results we will display.
     */

    @Override
    public void bindView(View v, Context context, Cursor c) {
        String title = c.getString(c.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE));
        String img = c.getString(c.getColumnIndexOrThrow(NotesDbAdapter.KEY_ICON));
        ImageView imageView = (ImageView) v.findViewById(R.id.img);
        
        InputStream is = context.getResources().openRawResource(IconBank.icons.get(img));
        Bitmap originalBitmap = BitmapFactory.decodeStream(is);  
        imageView.setImageBitmap(originalBitmap);
        
        TextView title_text = (TextView) v.findViewById(R.id.title);
        if (title_text != null) {
            title_text.setText(title);
        }
    }
}