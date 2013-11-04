package com.example.savedisplay;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class MainActivity extends Activity
{
	ArrayList<Data> data = null;
	MyCustomAdapter dataAdapter = null;
	final String SAVED_FILE = "savedData.txt";
	final String SEPARATOR = "@";

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Generate list View from ArrayList
		//displayListView();
		Log.d("ATag", "On create");
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		displayListView();
	}
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		int noOfCellsChecked = 0;
		
		for(int i = 0; i < data.size(); i++)
		{
			Data dataIterator = data.get(i);
			if(dataIterator.isSelected())
			{
				++noOfCellsChecked;
			}
		}
		
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    Log.d("SomeTag", "Recreating the action bar||" + String.valueOf(noOfCellsChecked));
	    if (noOfCellsChecked == 0)
		{
	    	menu.getItem(1).setEnabled(false);
		    menu.getItem(2).setEnabled(false);
		}
	    else
	    if(noOfCellsChecked >= 2)
	    {
	    	menu.getItem(1).setEnabled(false);
	    }
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.action_new:
			{
				Toast.makeText(this, "action_new selected", Toast.LENGTH_SHORT).show();
				Intent intentToCreate = new Intent(this, EditActivity.class);
				startActivity(intentToCreate);
				break;
			}
			case R.id.action_edit:
			{
				Toast.makeText(this, "action_edit selected", Toast.LENGTH_SHORT).show();
				Intent intentToEdit = new Intent(this, EditActivity.class);
//				Bundle b = new Bundle();
//				b.put
//				startActivity(intentToEdit);
				break;
			}
			case R.id.action_discard:
			{
				Toast.makeText(this, "action_discard selected", Toast.LENGTH_SHORT).show();
				File fileToUpdate = new File(SAVED_FILE);
				fileToUpdate.delete();
				
				for(int i = 0; i < data.size(); i++)
				{
					Data dataIterator = data.get(i);
					if(dataIterator.isSelected())
					{						
						data.remove(i);
					}
					else
					{
						writeToFile(dataIterator.name + SEPARATOR, Context.MODE_APPEND);
					}
				}				
				
				dataAdapter.notifyDataSetInvalidated();
				
				break;
			}
			default:
				break;
		}
		
		return true;
	}

	private void displayListView() 
	{
		//Array list of countries
		data = new ArrayList<Data>();
				
		String stringFromFile = readFromFile();
		Log.d("MainActivity", "FILE READ: " + stringFromFile);
		
		String[] entries = stringFromFile.split("@");
		
		for (int i = 0; i < entries.length; i++)
		{
			Data entry = new Data(entries[i], false);
			data.add(entry);
		}	
		
		//create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this,
				R.layout.country_info, data);
		ListView listView = (ListView) findViewById(R.id.listView1);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);
	}

	private class MyCustomAdapter extends ArrayAdapter<Data> 
	{

		private ArrayList<Data> dataArray;

		public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Data> countryList) 
		{
			super(context, textViewResourceId, countryList);
			this.dataArray = new ArrayList<Data>();
			this.dataArray.addAll(countryList);
		}

		private class ViewHolder 
		{			
			CheckBox name;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{

			ViewHolder holder = null;
			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) 
			{
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.country_info, null);

				holder = new ViewHolder();
				holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
				convertView.setTag(holder);

				holder.name.setOnClickListener(new View.OnClickListener() 
				{  
					public void onClick(View v) 
					{  						
						CheckBox cb = (CheckBox) v ;  
						Data data = (Data) cb.getTag();  
						Toast.makeText(getApplicationContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_SHORT).show();
						data.setSelected(cb.isChecked());
						invalidateOptionsMenu();
					}  
				});  
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}

			Data data = dataArray.get(position);
			holder.name.setText(data.getName());
			holder.name.setChecked(data.isSelected());
			holder.name.setTag(data);

			return convertView;
		}

	}
	
	private String readFromFile() 
	{

	    String ret = "";

	    try 
	    {
	        InputStream inputStream = openFileInput(SAVED_FILE);

	        if ( inputStream != null ) 
	        {
	        	InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) 
	            {
	                stringBuilder.append(receiveString);
	            }

	            inputStream.close();
	            ret = stringBuilder.toString();
	        }
	    }
	    catch (FileNotFoundException e) 
	    {
	        Log.e("login activity", "File not found: " + e.toString());
	    } 
	    catch (IOException e) 
	    {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }

	    return ret;
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
