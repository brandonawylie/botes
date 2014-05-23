package com.brandon.botes;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class IconChooserAdapter extends ArrayAdapter<String>{
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