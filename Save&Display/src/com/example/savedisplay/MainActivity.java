package com.example.savedisplay;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	ArrayList<Country> countryList = null;
	MyCustomAdapter dataAdapter = null;
	final String SAVED_FILE = "savedData.txt";

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Generate list View from ArrayList
		displayListView();
		Log.d("ATag", "On create");
	}
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		int noOfCellsChecked = 0;
		ArrayList<Country> countryList = dataAdapter.countryList;
		
		for(int i = 0; i < countryList.size(); i++)
		{
			Country iteratorCountry = countryList.get(i);
			if(iteratorCountry.isSelected())
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
				break;
			}
			case R.id.action_discard:
			{
				Toast.makeText(this, "action_discard selected", Toast.LENGTH_SHORT).show();
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
		countryList = new ArrayList<Country>();
		Country country = new Country("Afghanistan", false);
		countryList.add(country);
		country = new Country("Albania", false);
		countryList.add(country);
		country = new Country("Algeria", false);
		countryList.add(country);
		country = new Country("American Samoa", false);
		countryList.add(country);
		country = new Country("Andorra", false);
		countryList.add(country);
		country = new Country("Angola", false);
		countryList.add(country);
		country = new Country("Anguilla", false);
		countryList.add(country);
		
		String stringFromFile = readFromFile();
		Log.d("MainActivity", "FILE READ: " + stringFromFile);
		country = new Country(stringFromFile, false);

		//create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this,
				R.layout.country_info, countryList);
		ListView listView = (ListView) findViewById(R.id.listView1);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);


//		listView.setOnItemClickListener(new OnItemClickListener() 
//		{
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
//			{
//				// When clicked, show a toast with the TextView text
//				
//				
//				Country country = (Country) parent.getItemAtPosition(position);
//				Toast.makeText(getApplicationContext(), "Clicked on Row: " + country.getName(), Toast.LENGTH_SHORT).show();
//			}
//		});

	}

	private class MyCustomAdapter extends ArrayAdapter<Country> 
	{

		private ArrayList<Country> countryList;

		public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Country> countryList) 
		{
			super(context, textViewResourceId, countryList);
			this.countryList = new ArrayList<Country>();
			this.countryList.addAll(countryList);
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
						Country country = (Country) cb.getTag();  
						Toast.makeText(getApplicationContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_SHORT).show();
						country.setSelected(cb.isChecked());
						invalidateOptionsMenu();
					}  
				});  
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}

			Country country = countryList.get(position);
			holder.name.setText(country.getName());
			holder.name.setChecked(country.isSelected());
			holder.name.setTag(country);

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
}
