package fyp.sbarcoe.tabsswipe;


import info.androidhive.tabsswipe.R;
import info.androidhive.tabsswipe.adapter.ImageLoader;
import info.androidhive.tabsswipe.adapter.TabsPagerAdapter;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	
	String stopName, returnString, returnString2, returnURL ;
	int stopZone ;
	String resultBuy, result2 ;
	static String image_url;
    public static ImageView image;
	int loader;

	//TextView userBal;
	Dialog mDialog;
	AQuery myimg;
	ImageView myimg2 ;
	SharedPreferences mPrefs;
    final String welcomeScreenShownPref = "welcomeScreenShown";
    
	// Tab titles
	private String[] tabs = { "Buy New", "Top Up", "Validate" };

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		//userBal = (TextView) findViewById(fyp.sbarcoe.tabsswipe.TopUp.topUpInt);
		mDialog = new ProgressDialog(MainActivity.this);

        

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
	        finish();
	    }
		

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
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));			
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
			public void onPageScrolled(int arg0, float arg1, int arg2) 
			{
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
	public void onTabReselected(Tab tab, FragmentTransaction ft) 
	{
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) 
	{
		// on tab selected
		// show respected fragment view
		String testTab = tab.getText().toString();

   	    if(testTab == "Top Up")
   	    {
        	new GetBal().execute(""); 
   	    }
   	    else if(testTab == "Buy New")
   	    {

   	    }
   	    else if(testTab == "Validate")
   	    {
        	new GetImageName().execute("");
   	    }
	}
 	       
	

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	class GetBal extends AsyncTask<String, Void, String> 
	{
		

        @Override
        protected String doInBackground(String... params) 
        {
        	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

            // define the parameter        cardnumber, expmonth, expyear, cv
            postParameters.add(new BasicNameValuePair("email", getEmail()));

            String response2 = null;
           // boolean success = false ;
                
            // call executeHttpPost method passing necessary parameters 
            try 
             {            	
            	response2 = CustomHttpClient.executeHttpPost("http://sbarcoe.net23.net/Android/getBal.php", postParameters);                         
            	resultBuy = response2.toString(); 
            	
              }
              catch (Exception e) 
              {
                   Log.e("log_tag","Error in http connection!!" + e.toString());               
              	   //Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();                      
              }
			  return response2; 
        }
        @Override
        protected void onPostExecute(String result2) 
        {          
        	
			mDialog.dismiss();
        	 //String remoteBal = Get Balance from Remote
        	 // updateLocalBal() ;
			//parse json data
             try
             {
            	 returnString = "";
                 JSONArray jArray = new JSONArray(result2);
                 Log.i("tagconvertstr", "["+resultBuy+"]");
                     for(int i=0;i<jArray.length();i++)
                     {
                             JSONObject json_data = jArray.getJSONObject(i);                            
                             returnString +=  json_data.getInt("Balance");
                             System.out.print("Returned: "+returnString);                        	 
                     }                     
              }
             catch(JSONException e)
             {
                     Log.e("log_tag", "Error parsing data "+e.toString());
             }    
             //userBal.append(returnString);
             fyp.sbarcoe.tabsswipe.TopUp.userBal.setText("CurrentBalance: €"+returnString);

        	// Toast.makeText(getApplicationContext(), "Bal: "+returnString, Toast.LENGTH_SHORT).show();                      

             //userBal.setText("Current Balance: "+returnString+"");               	
        }

        @Override
        protected void onPreExecute() 
        {
        	((AlertDialog) mDialog).setMessage("Updating Balance...");
            mDialog.show();             
        }
    }class GetImageName extends AsyncTask<String, Void, String> 
	{	

        @Override
        protected String doInBackground(String... params) 
        {
        	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

            // define the parameter        cardnumber, expmonth, expyear, cv
            postParameters.add(new BasicNameValuePair("email", getEmail()));

            String response2 = null;
           // boolean success = false ;
                
            // call executeHttpPost method passing necessary parameters 
            try 
             {            	
            	response2 = CustomHttpClient.executeHttpPost("http://sbarcoe.net23.net/Android/getQRCodeImg.php", postParameters);                         
            	result2 = response2.toString(); 
            	
              }
              catch (Exception e) 
              {
                   Log.e("log_tag","Error in http connection!!" + e.toString());               
              	   Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();                      
              }
			  return response2; 
        }
        @Override
        protected void onPostExecute(String result2) 
        {          
        	 mDialog.dismiss();
        	 //String remoteBal = Get Balance from Remote
        	 // updateLocalBal() ;
        	 
        	//parse json data
             try
             {
            	 returnString2 = "";
                 JSONArray jArray = new JSONArray(result2);
                 Log.i("tagconvertstr", "["+result2+"]");
                     for(int i=0;i<jArray.length();i++)
                     {
                             JSONObject json_data = jArray.getJSONObject(i);                            
                             returnString2 +=  json_data.getString("ticketimg");
                             System.out.print("Returned: "+returnString2);                        	 
                     }  
                     image_url = image_url+returnString2 ;                     
               	     ImageLoader imgLoader = new ImageLoader(getApplicationContext());
                     imgLoader.DisplayImage(image_url, loader, image);
                	 //Toast.makeText(getApplicationContext(), "Image URL: "+image_url, Toast.LENGTH_SHORT).show();
              }
             catch(JSONException e)
             {
                     Log.e("log_tag", "Error parsing data "+e.toString());
             }         
             //userBal.setText("Current Balance: €"+returnString+"");               	
        }

        @Override
        protected void onPreExecute() 
        {
            loader = R.drawable.loader;
            image_url = "http://sbarcoe.net23.net/Android/phpqrcode/tickets/";
    		image = (ImageView) findViewById(R.id.image);

        	((AlertDialog) mDialog).setMessage("Retrieving Ticket...");
            mDialog.show();             
        }
}
	
	private String getEmail() 
	{
		DBManager myDB = new DBManager(getApplicationContext());
		myDB.open();	
		String emailReturn ;
		emailReturn = myDB.getEmail();	
		myDB.close();
		return emailReturn;
	}

}
