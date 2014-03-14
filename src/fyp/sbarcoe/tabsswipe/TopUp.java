package fyp.sbarcoe.tabsswipe;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fyp.sbarcoe.tabsswipe.LuasPurchase.GetBal;


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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TopUp extends Activity implements OnItemSelectedListener
{
	String topUpAmt, currentBal, returnString ;
	String result2 ;
	Spinner topUp ;
	static TextView userBal ;
	Button topUpButton ;
	static int topUpInt ;
	ProgressDialog mDialog ;
	Boolean internetCheck ;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top_up);		
		getActionBar().setDisplayHomeAsUpEnabled(true);

		topUp = (Spinner) findViewById(R.id.spinnerTopUp);				
		topUp.setOnItemSelectedListener(this);
		topUpButton = (Button) findViewById(R.id.bTopUp);
		ArrayAdapter<CharSequence> topUpAdapter = ArrayAdapter.createFromResource(this,
				R.array.top_up, android.R.layout.simple_spinner_item);	
		
		topUpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		topUp.setAdapter(topUpAdapter);	
		userBal = (TextView) findViewById(R.id.tvTopUpBal);
		mDialog = new ProgressDialog(this);
		
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
        	StringBuilder sb = new StringBuilder();
    		sb.append("");
    		sb.append(topUpAmt);
    		String topUpAmtConv = sb.toString();
    				
            // define the parameter        cardnumber, expmonth, expyear, cv
            postParameters.add(new BasicNameValuePair("email", getEmail()));
            postParameters.add(new BasicNameValuePair("topUpAmt", topUpAmtConv));

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
        	 //String remoteBal = Get Balance from Remote
        	 // updateLocalBal() ; 
        	 
        	 if  (result.contains("Success"))
             {
             	Toast.makeText(getApplicationContext(), "You have topped up by "+"�"+topUpAmt+"", Toast.LENGTH_LONG).show(); 
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
        	 //String remoteBal = Get Balance from Remote
        	 // updateLocalBal() ;
        	 
        	//parse json data
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
             userBal.setText("Current Balance: �"+returnString+"");               	
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
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
	{
		topUpAmt = topUp.getSelectedItem().toString();
		topUpInt = Integer.parseInt(topUpAmt);	
		
		//int year1 = Integer.parseInt(month);	
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

		// deduct from card topUpInt if card balance > topUpInt
		//(Update cards SET Balance = Balance - topUpInt
	    //WHERE Email = post(email)		


}
