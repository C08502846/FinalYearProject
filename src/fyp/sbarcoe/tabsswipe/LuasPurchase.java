package fyp.sbarcoe.tabsswipe;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.tabsswipe.R;
import info.androidhive.tabsswipe.R.layout;
import info.androidhive.tabsswipe.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class LuasPurchase extends Activity 
{	
	Spinner luasLine, luasFrom, luasTo ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_luas_purchase);	
		
		luasLine = (Spinner) findViewById(R.id.spinnerLuasLine);
		luasFrom = (Spinner) findViewById(R.id.spinnerLuasFrom);
		luasTo = (Spinner) findViewById(R.id.spinnerLuasTo);
		
		ArrayAdapter<CharSequence> luasLineAdapter = ArrayAdapter.createFromResource(this,
		R.array.luas_line, android.R.layout.simple_spinner_item);				
		ArrayAdapter<CharSequence> luasFromAdapter = ArrayAdapter.createFromResource(this,
		R.array.luas_from, android.R.layout.simple_spinner_item);	
		ArrayAdapter<CharSequence> luasToAdapter = ArrayAdapter.createFromResource(this,
		R.array.luas_to, android.R.layout.simple_spinner_item);	
		
		luasLineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		luasFromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		luasToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		luasLine.setAdapter(luasLineAdapter);	
		luasFrom.setAdapter(luasFromAdapter);	
		luasTo.setAdapter(luasToAdapter);	
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.testmenu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch(item.getItemId())
		{
		case R.id.help:
			aboutMenuItem();
			break;
		case R.id.favourites:
		    settingMenuItem();
		    break;
		}
		return true;
	}
	private void aboutMenuItem() 
	{
		new AlertDialog.Builder(this)
		.setTitle("Help")
		.setMessage("This is Help Dialog")
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
									
			}
		}).show();		
	}
	
	private void settingMenuItem() 
	{		
		new AlertDialog.Builder(this)
		.setTitle("Favourites")
		.setMessage("This is Favourites Dialog")
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
									
			}
		}).show();
	}

}
