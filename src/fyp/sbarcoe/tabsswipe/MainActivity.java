package fyp.sbarcoe.tabsswipe;


import info.androidhive.tabsswipe.R;
import info.androidhive.tabsswipe.adapter.TabsPagerAdapter;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	
	String stopName ;
	int stopZone ;
	
	SharedPreferences mPrefs;
    final String welcomeScreenShownPref = "welcomeScreenShown";
    
	// Tab titles
	private String[] tabs = { "Buy New", "Top Up", "Validate" };

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		 // second argument is the default to use if the preference can't be found
	    Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);
	    if (!welcomeScreenShown) 
	    {	        
	    	final Intent i = new Intent(getApplicationContext(), Welcome.class);
		    startActivity(i);		    
	    	SharedPreferences.Editor editor = mPrefs.edit();
	        editor.putBoolean(welcomeScreenShownPref, true);
	        editor.commit(); // Very important to save the preference
	    }
		//insertStops();


		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		//actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		

		// Adding Tabs
		for (String tab_name : tabs) 
		{
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));			
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() 
		{

			@Override
			public void onPageSelected(int position) 
			{
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
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
			helpMenuItem();
			break;
		case R.id.favourites:
		    favouriteMenuItem();
		    break;		
		}
		return true;
	}
	public void insertStops()
	{
		DBManager myDB = new DBManager(this);
		myDB.open();
		
		//myDB.populateLuasData();		
		//myDB.addLuasStops(stopName, stopZone)		
		
		myDB.addLuasStops("Stephens Green", 1);
		myDB.addLuasStops("Harcourt", 1);
		myDB.addLuasStops("Charlemont", 1);
		myDB.addLuasStops("Ranelagh", 2);
		myDB.addLuasStops("Beechwood", 2);
		myDB.addLuasStops("Cowper", 2);
		myDB.addLuasStops("Milltown", 2);
		myDB.addLuasStops("Windy Arbour", 2);
		myDB.addLuasStops("Dundrum", 2);
		myDB.addLuasStops("Balally", 3);
		myDB.addLuasStops("Kilmacud", 3);
		myDB.addLuasStops("Stilorgan", 3);
		myDB.addLuasStops("Sandyford", 3);
		myDB.addLuasStops("Central Park", 4);
		myDB.addLuasStops("Glencairn", 4);
		myDB.addLuasStops("The Gallops", 4);
		myDB.addLuasStops("Leopardstown Valley", 4);
		myDB.addLuasStops("Ballyogan", 4);
		myDB.addLuasStops("Carrickmines", 5);
		myDB.addLuasStops("Laughanstown", 5);
		myDB.addLuasStops("Cherrywood", 5);
		myDB.addLuasStops("Brides Glen", 5);
		
		myDB.close();	
	}
	private void helpMenuItem() 
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
	
	private void favouriteMenuItem() 
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
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

}
