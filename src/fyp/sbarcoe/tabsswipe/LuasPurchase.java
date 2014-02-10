package fyp.sbarcoe.tabsswipe;

import info.androidhive.tabsswipe.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class LuasPurchase extends Activity 
{	
	Spinner luasLine, luasFrom, luasTo ;
	ArrayAdapter<String> luas_adp ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_luas_purchase);	
		DBManager myDB = new DBManager(this);
		myDB.open();
		String[] result = myDB.getStopNames() ;	
		
		for(int i = 0 ; i<result.length;i++)
		{
			System.out.println(result[i] +"\n");
		}		
		myDB.close();
		
		luasLine = (Spinner) findViewById(R.id.spinnerLuasLine);
		luasFrom = (Spinner) findViewById(R.id.spinnerLuasFrom);
		luasTo = (Spinner) findViewById(R.id.spinnerLuasTo);
		
		luas_adp = new ArrayAdapter<String> (this,android.R.layout.simple_dropdown_item_1line,result);
		luas_adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		
	    luasTo.setAdapter(luas_adp);
	    luasFrom.setAdapter(luas_adp);    	    
		
		ArrayAdapter<CharSequence> luasLineAdapter = ArrayAdapter.createFromResource(this,
		R.array.luas_line, android.R.layout.simple_spinner_item);			
		
//		ArrayAdapter<CharSequence> luasFromAdapter = ArrayAdapter.createFromResource(this,
//		R.array.luas_from, android.R.layout.simple_spinner_item);
		
		//ArrayAdapter<CharSequence> luasToAdapter = ArrayAdapter.createFromResource(this,
		//R.array.luas_to, android.R.layout.simple_spinner_item);	
		
		luasLineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//luasFromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//luasToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		luasLine.setAdapter(luasLineAdapter);	
		//luasFrom.setAdapter(luasFromAdapter);	
		//luasTo.setAdapter(luasToAdapter);	
				
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
		case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;			
		}
		return super.onOptionsItemSelected(item);
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
