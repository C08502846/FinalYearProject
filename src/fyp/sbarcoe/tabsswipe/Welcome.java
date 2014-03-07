package fyp.sbarcoe.tabsswipe;

import info.androidhive.tabsswipe.R;

import java.util.ArrayList;
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
		//insertStops();
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

	public void insertStops()
	{
		DBManager myDB = new DBManager(this);
		myDB.open();

		//myDB.populateLuasData();		
		//myDB.addLuasStops(stopName, stopZone)		

		//Green Line
		myDB.addLuasStops("Stephens Green","Green", 1);
		myDB.addLuasStops("Harcourt","Green", 1);
		myDB.addLuasStops("Charlemont", "Green",1);
		myDB.addLuasStops("Ranelagh", "Green",2);
		myDB.addLuasStops("Beechwood","Green", 2);
		myDB.addLuasStops("Cowper","Green", 2);
		myDB.addLuasStops("Milltown","Green", 2);
		myDB.addLuasStops("Windy Arbour", "Green",2);
		myDB.addLuasStops("Dundrum", "Green",2);
		myDB.addLuasStops("Balally", "Green",3);
		myDB.addLuasStops("Kilmacud","Green", 3);
		myDB.addLuasStops("Stilorgan","Green", 3);
		myDB.addLuasStops("Sandyford","Green", 3);
		myDB.addLuasStops("Central Park","Green", 4);
		myDB.addLuasStops("Glencairn","Green", 4);
		myDB.addLuasStops("The Gallops","Green", 4);
		myDB.addLuasStops("Leopardstown Valley","Green", 4);
		myDB.addLuasStops("Ballyogan","Green", 4);
		myDB.addLuasStops("Carrickmines","Green", 5);
		myDB.addLuasStops("Laughanstown", "Green", 5);
		myDB.addLuasStops("Cherrywood","Green", 5);
		myDB.addLuasStops("Brides Glen","Green", 5);

		//Insert Red Line Stops
		myDB.addLuasStops("The Point","Red", 1);
		myDB.addLuasStops("Spencer Dock","Red", 1);
		myDB.addLuasStops("Mayor Square NCI","Red", 1);
		myDB.addLuasStops("Georges Dock","Red", 1);
		myDB.addLuasStops("Connolly","Red", 1);
		myDB.addLuasStops("Busaras", "Red",1);
		myDB.addLuasStops("Abbey Street","Red", 2);
		myDB.addLuasStops("Jervis","Red", 2);
		myDB.addLuasStops("Four Courts","Red", 2);
		myDB.addLuasStops("Smithfield","Red", 2);
		myDB.addLuasStops("Museum","Red", 2);
		myDB.addLuasStops("Heuston","Red", 3);
		myDB.addLuasStops("James","Red", 3);
		myDB.addLuasStops("Fatima","Red", 3);
		myDB.addLuasStops("Rialto","Red", 3);
		myDB.addLuasStops("Suir Road", "Red", 4);
		myDB.addLuasStops("Goldenbridge","Red", 4);
		myDB.addLuasStops("Drimnagh","Red", 4);
		myDB.addLuasStops("Blackhorse","Red", 4);
		myDB.addLuasStops("Bluebell","Red", 4);
		myDB.addLuasStops("Kylemore","Red", 4);
		myDB.addLuasStops("Red Cow", "Red", 5);
		myDB.addLuasStops("Kingswood", "Red", 5);
		myDB.addLuasStops("Belgard", "Red", 5);
		myDB.addLuasStops("Fettercairn", "Red", 5);
		myDB.addLuasStops("Cheeverstown", "Red", 5);
		myDB.addLuasStops("Citywest Campus", "Red", 5);
		myDB.addLuasStops("Fortunestown","Red", 5);
		myDB.addLuasStops("Cookstown", "Red", 5);
		myDB.addLuasStops("Hospital", "Red", 5);
		myDB.addLuasStops("Tallaght", "Red", 5);
		
		myDB.close();	
	}
//	public int createID()
//	{
//		Random r = new Random();
//		int i1 = r.nextInt(100000-0);
//		return i1;		
//	}

}
