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

public class BusPurchase extends Activity 
{	
	Spinner ticketType, stages ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_purchase);	
		
		ticketType = (Spinner) findViewById(R.id.spinner1);
		stages = (Spinner) findViewById(R.id.spinner2);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		R.array.ticket_type, android.R.layout.simple_spinner_item);				
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
		R.array.stages, android.R.layout.simple_spinner_item);	
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		ticketType.setAdapter(adapter);	
		stages.setAdapter(adapter2);			
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
