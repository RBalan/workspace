package com.example.savedisplay;

import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class EditActivity extends Activity 
{
	final String SAVED_FILE = "savedData.txt";
	final String SEPARATOR = "@";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}
	
	public void saveEntry(View v)
	{
		EditText editedText = (EditText)findViewById(R.id.editInfo);
		writeToFile(editedText.getText().toString() + SEPARATOR, Context.MODE_APPEND);
		
		Log.d("EditActivity", "Saved entry " + editedText.getText());
		finish();
		
	}
	
	private void writeToFile(String data, int mode) 
	{
	    try 
	    {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(SAVED_FILE, mode));
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) 
	    {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
	}


	

}
