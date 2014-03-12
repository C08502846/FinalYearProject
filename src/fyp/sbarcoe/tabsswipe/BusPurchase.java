package fyp.sbarcoe.tabsswipe;
import info.androidhive.tabsswipe.R;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class BusPurchase extends Activity 
{	
	Spinner busFrom, busTo ;
	String returnString, result2, line, fromStop, toStop, zoneFrom, totalCost, zoneTo, resultBal, resultBuy, ticketType;
	String[] result; 
	TextView userBal, costBus  ;
	ProgressDialog mDialog;
	Button buyBtn ;

	Button buy ;
	int zoneFromInt, zoneToInt, zoneDiff ;
	private RadioGroup radioLineGroup;
	public ArrayAdapter<String> bus_adp ;
	double costOfJourney;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_purchase);	
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		busFrom = (Spinner) findViewById(R.id.spinnerBusFrom);
		busTo = (Spinner) findViewById(R.id.spinnerBusTo);
		userBal = (TextView) findViewById(R.id.userBal);
		buyBtn = (Button) findViewById(R.id.btnBuyBusTick);
		costBus = (TextView) findViewById(R.id.tvCostBus);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		R.array.ticket_type, android.R.layout.simple_spinner_item);				
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
		R.array.stages, android.R.layout.simple_spinner_item);	
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mDialog = new ProgressDialog(BusPurchase.this);
		result = populateStops("Green");

    	new GetBal().execute("");   
    	bus_adp = new ArrayAdapter<String> (getApplicationContext(),android.R.layout.simple_dropdown_item_1line,result);
    	bus_adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		busTo.setAdapter(bus_adp);
		busFrom.setAdapter(bus_adp);  

		addRadioGroupListenersBus();
		setSpinnerListenersBus() ;
		btnClickedBus() ;
		ticketType = "Adult";
	}

		

	private void btnClickedBus() 
	{
		buyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) 
			{
				new PurchaseBusTicket().execute(""); 
				//updateRemoteJourney(email, fromStop, toStop, totalCost) ;
				// if user Balance > total cost do this
				// Get UserEmail, Stop From, Stop To, , Total Cost, Date
				// Send to Remote DB:  Insert INTO Journeys VALUES(email, stopFrom, stopTo, totalCost, date)
				//else Error, please top up.

			}
 
		});

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
	public void addRadioGroupListenersBus()
	{
			radioLineGroup = (RadioGroup) findViewById(R.id.radioADCHI);
			int selectedId = radioLineGroup.getCheckedRadioButtonId();
			//radioLineButton.setChecked(false);        
	        radioLineGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
	        {
	        	@Override
	            public void onCheckedChanged(RadioGroup group, int checkedId)
	            {
	                switch(checkedId)
	                {
	                case R.id.radioAdult:
	                	//luasFrom.clearFocus();
	                	//costBus.setText("Journey Cost: �");

	                	//luasFrom.setBackgroundColor(-16711936);
	                	//luasTo.setBackgroundColor(-16711936);
	    				result = populateStops("Green");	
	    				ticketType = "Adult";
	    			
	    				//bus_adp = new ArrayAdapter<String> (getApplicationContext(),android.R.layout.simple_dropdown_item_1line,result);
	    				//bus_adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    				//busTo.setAdapter(bus_adp);
	    			   // busFrom.setAdapter(bus_adp);
	                    break;
	                case R.id.radioChild:	   
	                	//luasFrom.clearFocus();
	            		//costBus.setText("Journey Cost: �");

	                	//luasFrom.setBackgroundColor(-65536);
	                	//luasTo.setBackgroundColor(-65536);
	    				result = populateStops("Red");
	    				ticketType = "Child";

	    				//bus_adp = new ArrayAdapter<String> (getApplicationContext(),android.R.layout.simple_dropdown_item_1line,result);
	    				//bus_adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    				//busTo.setAdapter(bus_adp);
	    			   // busFrom.setAdapter(bus_adp);   
	                    break;	      
	                }
	            }}); 
    }
	 private void setSpinnerListenersBus() 
	 {
		 busFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
			{
			    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
			    {
			        Object item = parent.getItemAtPosition(pos);
			        fromStop = item.toString() ;
			        zoneFrom = getStopZone(fromStop);
			        zoneFromInt = Integer.parseInt(zoneFrom);
					setZoneCost();
					costBus.setText("Journey Cost: �" +totalCost);
			    }
			    public void onNothingSelected(AdapterView<?> parent) {
			    }
			});
		 busTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
			{
			    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
			    {
			        Object item = parent.getItemAtPosition(pos);
			        toStop = item.toString() ;
			        zoneTo = getStopZone(toStop);
			        zoneToInt = Integer.parseInt(zoneTo);
					setZoneCost();
					costBus.setText("Journey Cost: �" +totalCost);

			        //Toast.makeText(getApplicationContext(), "Zone To: " +zoneToInt, Toast.LENGTH_SHORT).show();

			    }
			    public void onNothingSelected(AdapterView<?> parent) {
			    }
			});

	}
	public String[] populateStops(String stopSelected)
	{
		DBManager myDB = new DBManager(this);
		myDB.open();
		String[] result = myDB.getLuasStops(stopSelected) ;	
		//userBal = (TextView) findViewById(R.id.userBal);
		myDB.close(); 
		return result ;

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
             userBal.setText("Current Balance: �"+returnString+"");               	
        }

        @Override
        protected void onPreExecute() 
        {
        	mDialog.setMessage("Getting Balance...");
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
	class PurchaseBusTicket extends AsyncTask<String, Void, String> 
	{


        @Override
        protected String doInBackground(String... params) 
        {
        	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        	//(String email, String fromStop2, String toStop2, String totalCost2)
            // define the parameter        cardnumber, expmonth, expyear, cv
        	
            postParameters.add(new BasicNameValuePair("email", getEmail()));
            postParameters.add(new BasicNameValuePair("tickettype", ticketType));
            postParameters.add(new BasicNameValuePair("stopfrom", fromStop));
            postParameters.add(new BasicNameValuePair("stopto", toStop));
            postParameters.add(new BasicNameValuePair("cost", totalCost));

            String response2 = null;
           //boolean success = false ;
                
            // call executeHttpPost method passing necessary parameters 
            try 
             {            	
            	response2 = CustomHttpClient.executeHttpPost("http://sbarcoe.net23.net/Android/buyBus.php", postParameters);                         
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
           	    final Intent i = new Intent(getApplicationContext(), BusValidate.class);
		        startActivity(i);    	
	            finish();  
           	   
           	    new GetBal().execute("");
            	
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
        	mDialog.setMessage("Purchasing Bus Ticket...");
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
}
