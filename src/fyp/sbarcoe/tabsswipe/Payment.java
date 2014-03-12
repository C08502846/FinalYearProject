package fyp.sbarcoe.tabsswipe;

import info.androidhive.tabsswipe.R;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Payment extends Activity implements OnItemSelectedListener
{
	SharedPreferences mPrefs;
	Spinner spinMonth, spinYear ;
	EditText cardNumber, cv ;
	//ImageButton payPal ;

    final String welcomeScreenShownPref = "welcomeScreenShown";
    public String monthValue, yearValue ;
    Button Submit ;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		Submit = (Button) findViewById(R.id.buttonPayment);
		spinMonth = (Spinner) findViewById(R.id.spinMonth);
		spinYear = (Spinner) findViewById(R.id.spinYear);
		spinMonth.setOnItemSelectedListener(this);
		spinYear.setOnItemSelectedListener(this);
		cardNumber = (EditText) findViewById(R.id.cardNumber);
		cv = (EditText) findViewById(R.id.cv);
		//payPal = (ImageButton) findViewById(R.id.paypal);

		Integer[] months = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12};
		Integer[] years = new Integer[]{2014, 2015, 2016, 2017, 2018, 2019, 2020};
		ArrayAdapter<Integer> monthAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_list_item_activated_1, months);
		spinMonth.setAdapter(monthAdapter);
		ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(this,android.R.layout.select_dialog_item, years);
		spinYear.setAdapter(yearAdapter);		
		
		Submit.setOnClickListener(new View.OnClickListener() 
		{			
            public void onClick(View v) 
            {                    	
            	new RegisterCard().execute("");  
            }
		});		    
	}
    private class RegisterCard extends AsyncTask<String, Void, String> {
		
		private ProgressDialog mDialog = new ProgressDialog(Payment.this);

        @Override
        protected String doInBackground(String... params) 
        {
        	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    		String localEmail = getEmail();
            // define the parameter        cardnumber, expmonth, expyear, cv
            postParameters.add(new BasicNameValuePair("cardnumber", cardNumber.getText().toString()));
            postParameters.add(new BasicNameValuePair("expmonth", monthValue));
            postParameters.add(new BasicNameValuePair("expyear", yearValue));
            postParameters.add(new BasicNameValuePair("cv", cv.getText().toString()));
            postParameters.add(new BasicNameValuePair("email", localEmail));            

            String response = null;
            String result = null ;
           // boolean success = false ;
                
            // call executeHttpPost method passing necessary parameters 
            try 
             {
            	response = CustomHttpClient.executeHttpPost("http://sbarcoe.net23.net/Android/registerCard.php", postParameters);              
            	result = response.toString();  
              }
              catch (Exception e) 
              {
                   Log.e("log_tag","Error in http connection!!" + e.toString());               
              	   Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();                      
              }
			  return response; 
        }
        @Override
        protected void onPostExecute(String result) 
        {           
        	 mDialog.dismiss();

        	 if  (result.contains("Success"))
             {
             	Toast.makeText(getApplicationContext(), "Card Added", Toast.LENGTH_SHORT).show();        	
                final Intent i = new Intent(getApplicationContext(), MainActivity.class);
             	startActivity(i);
             	finish();
             }
             else if(result.contains("Exists"))
             {
             	Toast.makeText(getApplicationContext(), "Invalid Card", Toast.LENGTH_SHORT).show();
             }
             else
             {
            	 Toast.makeText(getApplicationContext(), "No Result.", Toast.LENGTH_SHORT).show();
             }
        }

        @Override
        protected void onPreExecute() 
        {
        	mDialog.setMessage("Registering Card Details...");
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	// Inflate the menu; this adds items to the action bar if it is present.
	    getMenuInflater().inflate(R.menu.payment, menu);
	    return true;
    }
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
	{
		monthValue = spinMonth.getSelectedItem().toString();
		yearValue = spinYear.getSelectedItem().toString();
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
