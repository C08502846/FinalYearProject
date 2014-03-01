package fyp.sbarcoe.tabsswipe;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fyp.sbarcoe.tabsswipe.TopUp.GetBal;

import info.androidhive.tabsswipe.R;
import info.androidhive.tabsswipe.R.layout;
import info.androidhive.tabsswipe.R.menu;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BusPurchase extends Activity 
{	
	Spinner ticketType, stages ;
	String returnString, result;
	TextView userBal ;
	ProgressDialog mDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_purchase);	
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		ticketType = (Spinner) findViewById(R.id.spinner1);
		stages = (Spinner) findViewById(R.id.spinner2);
		userBal = (TextView) findViewById(R.id.curBal);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		R.array.ticket_type, android.R.layout.simple_spinner_item);				
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
		R.array.stages, android.R.layout.simple_spinner_item);	
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mDialog = new ProgressDialog(getApplicationContext());

		ticketType.setAdapter(adapter);	
		stages.setAdapter(adapter2);	
    	new GetBal().execute("");  

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
            	result = response2.toString(); 
            	
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
        	 //mDialog.dismiss();
        	 //String remoteBal = Get Balance from Remote
        	 // updateLocalBal() ;
        	 
        	
			//parse json data
             try
             {
            	 returnString = "";
                 JSONArray jArray = new JSONArray(result2);
                 Log.i("tagconvertstr", "["+result+"]");
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
             userBal.append(returnString);
             //userBal.setText("Current Balance: "+returnString+"");               	
        }

        @Override
        protected void onPreExecute() 
        {
        	//mDialog.setMessage("Getting Balance...");
           // mDialog.show();             
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
