package fyp.sbarcoe.tabsswipe;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fyp.sbarcoe.tabsswipe.BusPurchase.GetBal;
import info.androidhive.tabsswipe.R;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class LuasPurchase extends Activity implements OnItemSelectedListener
{	
	Spinner luasLine, luasFrom, luasTo ;
	ArrayAdapter<String> luas_adp ;
	String returnString, result;
	TextView userBal ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_luas_purchase);
		
		String[] result = populateGreenStops();
		
		luasLine = (Spinner) findViewById(R.id.spinnerLuasLine);
		luasFrom = (Spinner) findViewById(R.id.spinnerLuasFrom);
		luasTo = (Spinner) findViewById(R.id.spinnerLuasTo);
		
		luas_adp = new ArrayAdapter<String> (this,android.R.layout.simple_dropdown_item_1line,result);
		luas_adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		
	    luasTo.setAdapter(luas_adp);
	    luasFrom.setAdapter(luas_adp);    	    
		
		ArrayAdapter<CharSequence> luasLineAdapter = ArrayAdapter.createFromResource(this,
		R.array.luas_line, android.R.layout.simple_spinner_item);			
		
//		ArrayAdapter<CharSequence> luasFromAdapter = ArrayAdapter.createFromResource(this,
//		R.array.luas_from, android.R.layout.simple_spinner_item);
		
		//ArrayAdapter<CharSequence> luasToAdapter = ArrayAdapter.createFromResource(this,
		//R.array.luas_to, android.R.layout.simple_spinner_item);	
		
		luasLineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//luasFromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//luasToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		luasLine.setAdapter(luasLineAdapter);	
    	insertStops();

		luasLine.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) 
		    {
		    	String lineSelected = luasLine.getSelectedItem().toString();
             	//Toast.makeText(getApplicationContext(), "Luas Line: "+lineSelected, Toast.LENGTH_LONG).show(); 
		    	if(lineSelected == "Green")
		    	{
		    		//Empty Spinner from previous selection
		    		//getStops(lineSelected) ;
		    		// Populate Spinners With Green line Stops
		    		// Get Zone for current selection 
		    	}
		    	else if (lineSelected == "Red")
		    	{
		    		//Empty Spinner from previous selection
		    		//getStops(lineSelected) ;
		    		// Populate Spinners With Red line Stops
		    		// Get zone for current selection 
		    	}
		    	// Compare 2 Zones and Calculate Fare
		    	// Assign to ticketCost and display 
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		
		//luasFrom.setAdapter(luasFromAdapter);	
		//luasTo.setAdapter(luasToAdapter);	
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

            postParameters.add(new BasicNameValuePair("email", getEmail()));

            String response = null;
           // boolean success = false ;
                
            // call executeHttpPost method passing necessary parameters 
            try 
             {            	
            	response = CustomHttpClient.executeHttpPost("http://sbarcoe.net23.net/Android/getBal.php", postParameters);                         
            	result = response.toString(); 
            	
              }
              catch (Exception e) 
              {
                   Log.e("log_tag","Error in http connection!!" + e.toString());               
              	   //Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();                      
              }
			  return response; 
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
	public String[] populateGreenStops()
	{
		DBManager myDB = new DBManager(this);
		myDB.open();
		String[] result = myDB.getGreenStops() ;	
		userBal = (TextView) findViewById(R.id.userBal);
		myDB.close(); 
		return result ;
		
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
	{
		//topUpAmt = topUp.getSelectedItem().toString();
		//topUpInt = Integer.parseInt(topUpAmt);	
		
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	String[] getStops(String line)
	{
		return null;		
	}
	public void insertStops()
	{
		DBManager myDB = new DBManager(this);
		myDB.open();
		
		//myDB.populateLuasData();		
		//myDB.addLuasStops(stopName, stopZone)		
		
		//Green Line
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
		
		//Red Line
		
		
		myDB.close();	
	}
}

