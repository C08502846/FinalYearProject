package fyp.sbarcoe.tabsswipe;

import info.androidhive.tabsswipe.R;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LuasPurchase extends Activity implements OnItemSelectedListener
{	
	Spinner luasLine, luasFrom, luasTo ;
	public ArrayAdapter<String> luas_adp ;
	String returnString, resultBal, resultBuy, resultQR, fromStop, toStop, zoneFrom, zoneTo, totalCost ;
	String[] result; 
	String line ;
	TextView userBal, costLuas ;
	private RadioGroup radioLineGroup;
	private RadioButton radioLineButton;
	int zoneFromInt, zoneToInt, zoneDiff ;
	double costOfJourney;
	Button btnBuy ;
	ProgressDialog mDialog ;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_luas_purchase);
		luasFrom = (Spinner) findViewById(R.id.spinnerLuasFrom);
		luasTo = (Spinner) findViewById(R.id.spinnerLuasTo); 
		costLuas = (TextView) findViewById(R.id.tvCostLuas);
		btnBuy = (Button) findViewById(R.id.btnBuyLuasTick);
		userBal = (TextView) findViewById(R.id.userBal);
		mDialog = new ProgressDialog(LuasPurchase.this);
		addRadioGroupListeners();
		setSpinnerListeners() ;
		btnClicked() ;
    	//insertStops();
		zoneDiff = 0 ;
		line = "Green";
		result = populateStops("Green");
		luas_adp = new ArrayAdapter<String> (getApplicationContext(),android.R.layout.simple_dropdown_item_1line,result);
		luas_adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		luasTo.setAdapter(luas_adp);
	    luasFrom.setAdapter(luas_adp);   

		new GetBal().execute("");  
	}


	private void setZoneCost() 
	{
		if(zoneFromInt > zoneToInt)
		{
			zoneDiff = zoneFromInt - zoneToInt ;
			//Toast.makeText(getApplicationContext(), "From > To: " +zoneDiff, Toast.LENGTH_SHORT).show();

		}
		else if( zoneToInt > zoneFromInt)
		{
			zoneDiff = zoneToInt - zoneFromInt  ;
			//Toast.makeText(getApplicationContext(), "To > From: " +zoneDiff, Toast.LENGTH_SHORT).show();

		}
		else if(zoneFromInt == zoneToInt)
		{
			zoneDiff = 0 ;
			//Toast.makeText(getApplicationContext(), "Same: " +zoneDiff, Toast.LENGTH_SHORT).show();

		}
		// Green Line Peak Costs
		 switch (zoneDiff) 
		 {
         case 1:  zoneDiff = 1;
         costOfJourney = 1.70 ;
         break;
         case 2:  zoneDiff = 2;
         costOfJourney = 2.10 ;
         break;
         case 3:  zoneDiff = 3;
         costOfJourney = 2.50 ;
         break;
         case 4:  zoneDiff = 4;
         costOfJourney = 2.70 ;
         break;
         case 5:  zoneDiff = 5;
         costOfJourney = 2.90 ;
         break;        
         default: zoneDiff = 0;
         costOfJourney = 0 ;
         break;
         }
		 //convert to double
	 totalCost = String.valueOf(costOfJourney);


	}
	private void btnClicked() 
	{
		btnBuy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) 
			{
				new PurchaseTicket().execute(""); 
				//updateRemoteJourney(email, fromStop, toStop, totalCost) ;
				// if user Balance > total cost do this
				// Get UserEmail, Stop From, Stop To, , Total Cost, Date
				// Send to Remote DB:  Insert INTO Journeys VALUES(email, stopFrom, stopTo, totalCost, date)
				//else Error, please top up.

			}
 
		});

	}
	public void addRadioGroupListeners() 
	 {		 
			radioLineGroup = (RadioGroup) findViewById(R.id.radioColor);
			int selectedId = radioLineGroup.getCheckedRadioButtonId();
			radioLineButton = (RadioButton) findViewById(selectedId);
			//radioLineButton.setChecked(false);        
	        radioLineGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
	        {
	        	@Override
	            public void onCheckedChanged(RadioGroup group, int checkedId)
	            {
			    	String lineSelected = "";
			    	lineSelected = radioLineButton.getText().toString() ;
	                switch(checkedId)
	                {
	                case R.id.radioGreen:
	                	//luasFrom.clearFocus();
	                	costLuas.setText("Journey Cost: €");

	                	//luasFrom.setBackgroundColor(-16711936);
	                	//luasTo.setBackgroundColor(-16711936);
	    				result = populateStops("Green");	
	    				line = "Green";
	    				luas_adp = new ArrayAdapter<String> (getApplicationContext(),android.R.layout.simple_dropdown_item_1line,result);
	    				luas_adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    				luasTo.setAdapter(luas_adp);
	    			    luasFrom.setAdapter(luas_adp);
	                    break;
	                case R.id.radioRed:	   
	                	//luasFrom.clearFocus();
	            		costLuas.setText("Journey Cost: €");

	                	//luasFrom.setBackgroundColor(-65536);
	                	//luasTo.setBackgroundColor(-65536);
	    				result = populateStops("Red");
	    				line = "Red";
	    				luas_adp = new ArrayAdapter<String> (getApplicationContext(),android.R.layout.simple_dropdown_item_1line,result);
	    				luas_adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    				luasTo.setAdapter(luas_adp);
	    			    luasFrom.setAdapter(luas_adp);   
	                    break;	      
	                }
	            }}); 
		  }
	 private void setSpinnerListeners() 
	 {
		 luasFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
			{
			    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
			    {
			        Object item = parent.getItemAtPosition(pos);
			        fromStop = item.toString() ;
			        zoneFrom = getStopZone(fromStop);
			        zoneFromInt = Integer.parseInt(zoneFrom);
					setZoneCost();
					costLuas.setText("Journey Cost: €" +totalCost);
			    }
			    public void onNothingSelected(AdapterView<?> parent) {
			    }
			});
		 luasTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
			{
			    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
			    {
			        Object item = parent.getItemAtPosition(pos);
			        toStop = item.toString() ;
			        zoneTo = getStopZone(toStop);
			        zoneToInt = Integer.parseInt(zoneTo);
					setZoneCost();
					costLuas.setText("Journey Cost: €" +totalCost);

			        //Toast.makeText(getApplicationContext(), "Zone To: " +zoneToInt, Toast.LENGTH_SHORT).show();

			    }
			    public void onNothingSelected(AdapterView<?> parent) {
			    }
			});

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
        	mDialog.setMessage("Updating Balance...!!!!");
            mDialog.show();             
        }
    }
	class PurchaseTicket extends AsyncTask<String, Void, String> 
	{


        @Override
        protected String doInBackground(String... params) 
        {
        	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        	//(String email, String fromStop2, String toStop2, String totalCost2)
            // define the parameter        cardnumber, expmonth, expyear, cv
        	
            postParameters.add(new BasicNameValuePair("email", getEmail()));
            postParameters.add(new BasicNameValuePair("line", line));
            postParameters.add(new BasicNameValuePair("stopfrom", fromStop));
            postParameters.add(new BasicNameValuePair("stopto", toStop));
            postParameters.add(new BasicNameValuePair("cost", totalCost));

            String response2 = null;
           //boolean success = false ;
                
            // call executeHttpPost method passing necessary parameters 
            try 
             {            	
            	response2 = CustomHttpClient.executeHttpPost("http://sbarcoe.net23.net/Android/buyLuas.php", postParameters);                         
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
        	 
        	 if  (resultBuy.contains("Success"))
             {
           	    Toast.makeText(getApplicationContext(), "Ticket Purchased", Toast.LENGTH_SHORT).show();
           	              	  
           	    final Intent i = new Intent(getApplicationContext(), MainActivity.class);
          	    startActivity(i);
          	    finish();
           	    //new GetBal().execute("");
            	
            	//new CreateQRTicket().execute(""); 

             	//insertLocalUserData(email.getText().toString(), pw.getText().toString());
             }
             else if(resultBuy.contains("NoFunds"))
             {
             	Toast.makeText(getApplicationContext(), "Purchase Fail", Toast.LENGTH_SHORT).show();
             }
             else
             {
            	 Toast.makeText(getApplicationContext(), "No Result", Toast.LENGTH_SHORT).show();
             }
        	 // updateLocalBal() ;
             //userBal.setText("Current Balance: "+returnString+"");               	
        }

        @Override
        protected void onPreExecute() 
        {
        	mDialog.setMessage("Purchasing Ticket...");
            mDialog.show();             
        }
    }
	
	public String getStopZone(String stopName)
	{
		 String result2 ;
		 DBManager myDB = new DBManager(this);
		 myDB.open();
	     result2 = myDB.getZone(stopName) ;
	     myDB.close();
		 return result2;
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
	public String[] populateStops(String stopSelected)
	{
		DBManager myDB = new DBManager(this);
		myDB.open();
		String[] result = myDB.getStops(stopSelected) ;	
		//userBal = (TextView) findViewById(R.id.userBal);
		myDB.close(); 
		return result ;

	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
	{
		//topUpAmt = topUp.getSelectedItem().toString();
		//topUpInt = Integer.parseInt(topUpAmt);	
		//String lol = luasFrom.toString() ; 
   	   // Toast.makeText(getApplicationContext(), "From: " +lol, Toast.LENGTH_SHORT).show();


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
}