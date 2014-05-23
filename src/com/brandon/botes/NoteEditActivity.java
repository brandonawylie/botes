
package com.brandon.botes;

import java.io.InputStream;
import java.util.List;

import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NoteEditActivity extends Activity{

    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;
    private NotesDbAdapter mDbHelper;
    private String icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	getActionBar().setDisplayHomeAsUpEnabled(true);

    	mDbHelper = new NotesDbAdapter(this);
    	mDbHelper.open();//

    	setContentView(R.layout.note_edit);

    	mTitleText = (EditText) findViewById(R.id.title);
    	mBodyText = (EditText) findViewById(R.id.body);

    	Button confirmButton = (Button) findViewById(R.id.confirm);

    	mRowId = (savedInstanceState == null) ? null :
    	    (Long) savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
    	if (mRowId == null) {
    	    Bundle extras = getIntent().getExtras();
    	    mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID)
    	                            : null;
    	}
    	mTitleText.setFocusableInTouchMode(false);
    	mBodyText.setFocusableInTouchMode(false);
    	populateFields();

    	confirmButton.setOnClickListener(new View.OnClickListener() {

    	    public void onClick(View view) {
    	    	 setResult(RESULT_OK);
     	         finish();
    	    }

    	});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editnote_actions, menu); 
        return super.onCreateOptionsMenu(menu);
    }
    
    private void populateFields() {
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchNote(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(
                        note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
            mBodyText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));          
        }else{
        	mTitleText.setFocusableInTouchMode(true);
        	mBodyText.setFocusableInTouchMode(true);
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
    
    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();

        if (mRowId == null) {
            long id = mDbHelper.createNote(title, body);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateNote(mRowId, title, body);
        }
        
        if(icon != null)
        	mDbHelper.updateNote(mRowId, title, body, icon);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case android.R.id.home:
        	saveState();
        	setResult(RESULT_OK);
 	        finish();
            return true;
        case R.id.noteedit_edit:
        	//populateFields();
        	mTitleText.setFocusableInTouchMode(true);
        	mBodyText.setFocusableInTouchMode(true);
        	return true;
        case R.id.noteedit_view:
        	//populateFields();
        	mTitleText.setFocusableInTouchMode(false);
        	mBodyText.setFocusableInTouchMode(false);
        	return true;
        	
        case R.id.noteedit_icon:
        	showIconDialog();
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void showIconDialog(){
    	final Dialog dialog = new Dialog(this);
    	IconChooserAdapter adapter = new IconChooserAdapter(this);
    	ListView view = new ListView(this);
    	dialog.setContentView(view);
    	view.setAdapter(adapter);
    	view.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
				TextView textView = (TextView) v.findViewById(R.id.title);
				String title = (String) textView.getText();
				if(IconBank.icons.containsKey(title)){
					icon = title;
					saveState();
					dialog.dismiss();
					
				}
			}
    	
    	
    	});
    	dialog.show();
    }
    
    /**
     * Defines a custom EditText View that draws lines between each line of text that is displayed.
     */
    public static class LinedEditText extends EditText {
		private Rect mRect;
        private Paint mPaint;

        // This constructor is used by LayoutInflater
        public LinedEditText(Context context, AttributeSet attrs) {
            super(context, attrs);

            // Creates a Rect and a Paint object, and sets the style and color of the Paint object.
            mRect = new Rect();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.BLACK);
            
            
        }
        /**
         * This is called to draw the LinedEditText object
         * @param canvas The canvas on which the background is drawn.
         */
        @Override
	    protected void onDraw(Canvas canvas) {
            Rect bounds = new Rect();
            int firstLineY = getLineBounds(0, bounds);
            int lineHeight = getLineHeight();
            int totalLines = Math.max(getLineCount(), getHeight() / lineHeight);

            for (int i = 0; i < totalLines; i++) {
                int lineY = firstLineY + i * lineHeight;
                canvas.drawLine(bounds.left, lineY, bounds.right, lineY, mPaint);
            }

       
            // Finishes up by calling the parent method
            super.onDraw(canvas);
        }
    }
    
    public class MySimpleArrayAdapter extends ArrayAdapter<String> {
    	  private final Context context;
    	  private final String[] values;

    	  public MySimpleArrayAdapter(Context context, String[] values) {
    	    super(context, R.layout.notes_row, values);
    	    this.context = context;
    	    this.values = values;
    	  }

    	  @Override
    	  public View getView(int position, View convertView, ViewGroup parent) {
    		  LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		  View rowView = inflater.inflate(R.layout.notes_row, parent, false);
    		  TextView textView = (TextView) rowView.findViewById(R.id.title);
			  ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
			  textView.setText(values[position]);
			  
			  InputStream is = context.getResources().openRawResource(IconBank.icons.get(values[position]));
		      Bitmap originalBitmap = BitmapFactory.decodeStream(is);  
		      imageView.setImageBitmap(originalBitmap);
			return rowView;
			
    	} 
    }

	
	private static class IconChooserAdapter extends ArrayAdapter<String>{
		String[] values;
		Context context;
		public IconChooserAdapter(Context context) {
			super(context, R.layout.notes_row, IconBank.icons.keySet().toArray(new String[IconBank.icons.size()]));
			values = IconBank.icons.keySet().toArray(new String[IconBank.icons.size()]);
			this.context = context;
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			LayoutInflater inflater = (LayoutInflater) context
				        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.notes_row, parent, false);
			
			String title = values[position];
	        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
	        
	        InputStream is = context.getResources().openRawResource(IconBank.icons.get(title));
	        Bitmap originalBitmap = BitmapFactory.decodeStream(is);  
	        imageView.setImageBitmap(originalBitmap);
	        
	        TextView title_text = (TextView) rowView.findViewById(R.id.title);
	        if (title_text != null) {
	            title_text.setText(title);
	        }
	        return rowView;
		}
		
	}
}
