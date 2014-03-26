package fyp.sbarcoe;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fyp.sbarcoe.LuasPurchase.GetBal;


import info.androidhive.tabsswipe.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TopUp extends Activity
{
	String topUpAmt, currentBal, returnString ;
	String result2 ;
	static TextView userBal ;
	Button topUpButton ;
	static int topUpInt ;
	ProgressDialog mDialog ;
	Boolean internetCheck ;
	private RadioGroup radioTopUpGroup;
	private RadioButton radio5, radio10, radio15, radio20;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top_up);		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		addRadioGroupListeners() ;
		topUpButton = (Button) findViewById(R.id.bTopUp);
		ArrayAdapter<CharSequence> topUpAdapter = ArrayAdapter.createFromResource(this,
				R.array.top_up, android.R.layout.simple_spinner_item);	
		
		topUpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		userBal = (TextView) findViewById(R.id.tvTopUpBal);
		mDialog = new ProgressDialog(TopUp.this);
		
    	//new GetBal().execute("");  
        internetCheck = isOnline() ;
		
    	if(internetCheck)
    	{
    		new GetBal().execute(""); 
    	}
    	else
    	{
       	    Toast.makeText(getApplicationContext(), "No Internet Connection. Please check your network settings and try again.", Toast.LENGTH_SHORT).show();
    	}
		topUpButton.setOnClickListener(new View.OnClickListener() {public void onClick(View v)
		{				
			String localEmail = getEmail();
	   	    //deductFunds(localEmail, topUpInt);
			// new validateCV2().execute("");
        	new TopUp2().execute("");  
        	//new getUserBal().execute("");  
	   	   // topUpUser() ;
		}
		});			
	}	
	public void addRadioGroupListeners() 
	 {
		radioTopUpGroup = (RadioGroup) findViewById(R.id.radioTOPUP);
		int selectedId = radioTopUpGroup.getCheckedRadioButtonId();
	    radio5 = (RadioButton) findViewById(selectedId);
	    radio10 = (RadioButton) findViewById(selectedId);
	    radio15 = (RadioButton) findViewById(selectedId);
	    radio20 = (RadioButton) findViewById(selectedId);
	    
	    radioTopUpGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
       {
       	@Override
       	public void onCheckedChanged(RadioGroup group, int checkedId)
       	{
       		switch(checkedId)
       		{
       		case R.id.radioTOPUP5:	
       			topUpAmt = "5";
       			break;
       		case R.id.radioTOPUP10:  
       			topUpAmt = "10";
       			break;
       		case R.id.radioTOPUP15:  
       			topUpAmt = "15";
       			break;	
       		case R.id.radioTOPUP20:  
       			topUpAmt = "20";	
       			break;	       		
       		}
       	}});	
	 
	 }
	public boolean isOnline() 
	{
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	class TopUp2 extends AsyncTask<String, Void, String> 
	{		

        @Override
        protected String doInBackground(String... params) 
        {
        	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();        	
            // pass in email and otp up ammount to be added to account / deducted from card
            postParameters.add(new BasicNameValuePair("email", getEmail()));
            postParameters.add(new BasicNameValuePair("topUpAmt", topUpAmt));

            String response1 = null;
           // boolean success = false ;
                
            // call executeHttpPost method passing necessary parameters 
            try 
             {
            	response1 = CustomHttpClient.executeHttpPost("http://sbarcoe.net23.net/Android/deductCard.php", postParameters);                         
            	String result = response1.toString(); 
             }
              catch (Exception e) 
              {
                   Log.e("log_tag","Error in http connection!!" + e.toString());               
              	   //Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_SHORT).show();                      
              }
			  return response1; 
        }
        @Override
        protected void onPostExecute(String result) 
        {          
        	 mDialog.dismiss();
        	        	 
        	 if  (result.contains("Success"))
             {
             	Toast.makeText(getApplicationContext(), "You have topped up by "+"€"+topUpAmt+"", Toast.LENGTH_LONG).show(); 
             	final Intent i = new Intent(getApplicationContext(), MainActivity.class);
             	startActivity(i);
             	finish();
             }
             else if(result.contains("NoFunds"))
             {
             	Toast.makeText(getApplicationContext(), "Not enough funds", Toast.LENGTH_SHORT).show();
             }
             else
             {
            	 Toast.makeText(getApplicationContext(), "No Result.", Toast.LENGTH_SHORT).show();
             }
        }

        @Override
        protected void onPreExecute() 
        {
        	mDialog.setMessage("Topping up account...");
            mDialog.show();             
        }
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
            	result2 = response2.toString(); 
            	
              }
              catch (Exception e) 
              {
                   Log.e("log_tag","Error in http connection!!" + e.toString());               
              }
			  return response2; 
        }
        @Override
        protected void onPostExecute(String result2) 
        {          
        	 mDialog.dismiss();        
             try
             {
            	 returnString = "";
                 JSONArray jArray = new JSONArray(result2);
                 Log.i("tagconvertstr", "["+result2+"]");
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
             userBal.setText("Current Balance: €"+returnString+"");               	
        }

        @Override
        protected void onPreExecute() 
        {
        	mDialog.setMessage("Updating Balance...");
            mDialog.show();             
        }
}
	private String getEmail() 
	{
		DBManager myDB = new DBManager(this);
		myDB.open();	
		String emailReturn ;
		emailReturn = myDB.getEmail();	
		myDB.close();
		return emailReturn;
	}
}
