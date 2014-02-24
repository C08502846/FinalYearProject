package fyp.sbarcoe.tabsswipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import info.androidhive.tabsswipe.R;
import info.androidhive.tabsswipe.R.layout;
import info.androidhive.tabsswipe.R.menu;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class Payment extends Activity 
{
	SharedPreferences mPrefs;
	Spinner month, year ;
	ImageButton payPal ;

    final String welcomeScreenShownPref = "welcomeScreenShown";
    Button Submit ;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Submit = (Button) findViewById(R.id.buttonPayment);
		month = (Spinner) findViewById(R.id.spinMonth);
		year = (Spinner) findViewById(R.id.spinYear);
		
		payPal = (ImageButton) findViewById(R.id.paypal);

		Integer[] months = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12};
		Integer[] years = new Integer[]{2014, 2015, 2016, 2017, 2018, 2019, 2020};
		
		ArrayAdapter<Integer> monthAdapter = new ArrayAdapter<Integer>(this,android.R.layout.select_dialog_item, months);
		month.setAdapter(monthAdapter);
		ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(this,android.R.layout.select_dialog_item, years);
		year.setAdapter(yearAdapter);
	
		
		Submit.setOnClickListener(new View.OnClickListener() 
		{			
            public void onClick(View v) 
            { 
            	
            	final Intent i = new Intent(getApplicationContext(), MainActivity.class);
    		    startActivity(i);	            
            }
		});
		payPal.setOnClickListener(new View.OnClickListener() {public void onClick(View v)
		{ 
			// Handle Paypal transaction 
		}});
		
//		 Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);
//		    if (!welcomeScreenShown) 
//		    {	        
//		    	final Intent i = new Intent(getApplicationContext(), Welcome.class);
//			    startActivity(i);		    
//		    	SharedPreferences.Editor editor = mPrefs.edit();
//		        editor.putBoolean(welcomeScreenShownPref, true);
//		        editor.commit(); // Very important to save the preference
//		    }
		    
	}
    private class LongOperation extends AsyncTask<String, Void, String> {
		
		private ProgressDialog mDialog = new ProgressDialog(Payment.this);

        @Override
        protected String doInBackground(String... params) 
        {
        	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            
            // define the parameter        
            //postParameters.add(new BasicNameValuePair("email", email.getText().toString()));
           // postParameters.add(new BasicNameValuePair("pw", pw.getText().toString()));

            String response = null;
            String result = null ;
           // boolean success = false ;
                
            // call executeHttpPost method passing necessary parameters 
            try 
             {
            	response = CustomHttpClient.executeHttpPost("http://sbarcoe.net23.net/Android/insert2.php", postParameters);              
            	result = response.toString();  
              }
              catch (Exception e) 
              {
                   Log.e("log_tag","Error in http connection!!" + e.toString());               
              	   Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();                      
              }
			  return response; 
        }
	  
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	// Inflate the menu; this adds items to the action bar if it is present.
	    getMenuInflater().inflate(R.menu.payment, menu);
	    return true;
    }

}
