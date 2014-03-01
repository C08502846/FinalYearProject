package fyp.sbarcoe.tabsswipe;

import info.androidhive.tabsswipe.R;

import java.util.ArrayList;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Welcome extends Activity 
{
    Button continueB ; 
    int idGen ;
    EditText email, pw ;
    boolean successReturn ;
	ProgressDialog mDialog ;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);				
		email = (EditText) findViewById(R.id.email);
		pw = (EditText) findViewById(R.id.pw);		
		mDialog = new ProgressDialog(Welcome.this);

		continueB = (Button) findViewById(R.id.continueB);					
		continueB.setOnClickListener(new View.OnClickListener() 
		{			
            public void onClick(View v) 
            {                 
                String email1 = email.getText().toString(); 
                String pw1 = pw.getText().toString(); 
               
                if (email1.isEmpty() || pw1.isEmpty() )
                {
               	    Toast.makeText(getApplicationContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                	new RegisterUser().execute("");   
                }                                  
                
            	//registerUser(email1, pw1);            	
            	//Toast.makeText(getApplicationContext(), "Please try again.", Toast.LENGTH_SHORT).show();            	    
            }
        });
		
	}
	private class RegisterUser extends AsyncTask<String, Void, String> 
	{
		

        @Override
        protected String doInBackground(String... params) 
        {
        	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            
            // define the parameter        
            postParameters.add(new BasicNameValuePair("email", email.getText().toString()));
            postParameters.add(new BasicNameValuePair("pw", pw.getText().toString()));

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

        @Override
        protected void onPostExecute(String result) 
        {
            //TextView txt = (TextView) findViewById(R.id.output);
           // txt.setText("Executed"); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        
            mDialog.dismiss();

        	 if  (result.contains("Success"))
             {
             	Toast.makeText(getApplicationContext(), "Registered.", Toast.LENGTH_SHORT).show(); 
             	
             	insertLocalUserData(email.getText().toString(), pw.getText().toString());
             	
             	final Intent i = new Intent(getApplicationContext(), Payment.class);
             	startActivity(i);
             	Intent i1 = new Intent(Intent.ACTION_SEND);
             	i1.setType("message/rfc822");
             	i1.putExtra(Intent.EXTRA_EMAIL  , new String[]{"barcoe4@hotmail.com"});
             	i1.putExtra(Intent.EXTRA_SUBJECT, "Welcome to Leap Me");
             	i1.putExtra(Intent.EXTRA_TEXT   , "La la laa");
             	try 
             	{
             	    startActivity(Intent.createChooser(i1, "Send mail..."));
             	} 
             	catch (android.content.ActivityNotFoundException ex) 
             	{
             	    Toast.makeText(Welcome.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
             	}
             	
             	finish();
             }
             else if(result.contains("Exists"))
             {
             	Toast.makeText(getApplicationContext(), "User Already Exists.", Toast.LENGTH_SHORT).show();
             }
             else
             {
            	 Toast.makeText(getApplicationContext(), "No Result.", Toast.LENGTH_SHORT).show();
             }
        }

        @Override
        protected void onPreExecute() 
        {
        	mDialog.setMessage("Registering...");
            mDialog.show();       
            
        }

        @Override
        protected void onProgressUpdate(Void... values) 
        {
        	
        }
    }	
    public void insertLocalUserData(String Email, String Password)
    {
    	DBManager myDB = new DBManager(this);
		myDB.open();
		myDB.insertLocalUser(Email, Password);
		myDB.close();
		
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}
	public int createID()
	{
		Random r = new Random();
		int i1 = r.nextInt(100000-0);
		return i1;		
	}

}
