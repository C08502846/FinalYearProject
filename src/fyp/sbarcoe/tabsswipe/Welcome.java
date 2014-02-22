package fyp.sbarcoe.tabsswipe;

import info.androidhive.tabsswipe.R;

import java.util.ArrayList;
import java.util.Random;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome extends Activity 
{
    Button continueB ; 
    int idGen ;
    EditText email, pw ;
    boolean successReturn ;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);				
		email = (EditText) findViewById(R.id.email);
		pw = (EditText) findViewById(R.id.pw);		

		continueB = (Button) findViewById(R.id.continueB);					
		continueB.setOnClickListener(new View.OnClickListener() 
		{
			
            public void onClick(View v) 
            { 
                boolean fieldsEntered = false ;
                
                String email1 = email.getText().toString(); 
                String pw1 = pw.getText().toString(); 
               
                if (email1.isEmpty() || pw1.isEmpty() )
                {
               	    Toast.makeText(getApplicationContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                	new LongOperation().execute(""); 
                }    
                                 
                
            	//registerUser(email1, pw1);            	
            	//Toast.makeText(getApplicationContext(), "Please try again.", Toast.LENGTH_SHORT).show();
            	    
            }
        });
		
	}
	private class LongOperation extends AsyncTask<String, Void, String> {
		
		private ProgressDialog mDialog = new ProgressDialog(Welcome.this);

        @Override
        protected String doInBackground(String... params) 
        {
        	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            
            // define the parameter        
            postParameters.add(new BasicNameValuePair("email", email.getText().toString()));
            postParameters.add(new BasicNameValuePair("pw", pw.getText().toString()));

            String response = null;
            String result = null ;
            boolean success = false ;
                
            // call executeHttpPost method passing necessary parameters 
            try 
             {
              response = CustomHttpClient.executeHttpPost("http://sbarcoe.net23.net/Android/insert2.php", postParameters);
               //response = CustomHttpClient.executeHttpPost("http://sbarcoe.net23.net/Android/registerUser.php", postParameters);
               // store the result returned by PHP script that runs MySQL query
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

        	 if (result.contains("Success"))
             {
             	Toast.makeText(getApplicationContext(), "Registered.", Toast.LENGTH_SHORT).show();
                System.out.println("woohoo!");            	
             	final Intent i = new Intent(getApplicationContext(), MainActivity.class);
             	startActivity(i);
             	finish();
             }
             else if(result.contains("Exists"))
             {
             	Toast.makeText(getApplicationContext(), "User Already Exists.", Toast.LENGTH_SHORT).show();
                 System.out.println("Exists");
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
