package fyp.sbarcoe.tabsswipe;
import fyp.sbarcoe.tabsswipe.LuasPurchase.PurchaseTicket;
import info.androidhive.tabsswipe.R;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class BusPurchase extends Activity 
{	
	String returnString, result2, busRoute2, busStopFrom2, busStopTo2, totalCost, zoneTo, resultBal, resultBuy, ticketType;
	String[] busStops, busRoutes; 
	TextView userBal, costBus  ;
	ProgressDialog mDialog;
	Button buyBtn ;
	Boolean internetCheck ;
	Button buy, dialogButtonOK, dialogButtonNO  ;
	int zoneFromInt, zoneToInt, zoneDiff ;
	private RadioGroup radioLineGroup;
	public ArrayAdapter<String> bus_adp, bus_num_adp ;
	double costOfJourney, userBalance;	
	Dialog dialog;
	Context context ;
	AutoCompleteTextView busRoute, busStopFrom, busStopTo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_purchase);	
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mDialog = new ProgressDialog(BusPurchase.this);
		context = this;
		
		busRoute = (AutoCompleteTextView) findViewById(R.id.busRoute);
		busStopFrom = (AutoCompleteTextView) findViewById(R.id.autoBusFrom);
		busStopTo = (AutoCompleteTextView) findViewById(R.id.autoBusTo);

		//busFrom = (Spinner) findViewById(R.id.spinnerBusFrom);
		//busTo = (Spinner) findViewById(R.id.spinnerBusTo);
		userBal = (TextView) findViewById(R.id.userBal);
		buyBtn = (Button) findViewById(R.id.btnBuyBusTick);
		costBus = (TextView) findViewById(R.id.tvCostBus);		
		costBus.setText("2.10");
		Resources res = getResources();
		busStops = res.getStringArray(R.array.busStops);
		busRoutes  = res.getStringArray(R.array.busNums);
		internetCheck = isOnline() ;
		costOfJourney = 2.10;
		totalCost = String.valueOf(costOfJourney);
		busRoute2 = "";
		setUpDialogs();
    	if(internetCheck)
    	{
    	    new GetBal().execute(""); 
    	}
    	else
    	{
       	    Toast.makeText(getApplicationContext(), "No Internet Connection. Please check your network settings and try again.", Toast.LENGTH_SHORT).show();
    	}
    	
    	bus_adp = new ArrayAdapter<String> (getApplicationContext(),android.R.layout.simple_dropdown_item_1line,busStops);
    	bus_adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

    	bus_num_adp = new ArrayAdapter<String> (getApplicationContext(),android.R.layout.simple_dropdown_item_1line,busRoutes);
    	bus_num_adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);    	
    	
    	busRoute.setAdapter(bus_num_adp);
    	busStopFrom.setAdapter(bus_adp);
    	busStopTo.setAdapter(bus_adp);
		//busTo.setAdapter(bus_adp);
		//busFrom.setAdapter(bus_adp);  

		addRadioGroupListenersBus();
		//setAutoCompleteListenersBus();
		btnClickedBus() ;
		
		ticketType = "Child";
	}
	private void setUpDialogs() 
	{
		dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Insufficient Funds.");		
		
		// set the custom dialog components - text and button
		TextView text = (TextView) dialog.findViewById(R.id.DialogText);
		text.setText("Would You Like to top up?");
		
		dialogButtonOK = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButtonNO = (Button) dialog.findViewById(R.id.dialogButtonNO);	
		
		// if button is clicked, close the custom dialog
				dialogButtonOK.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						final Intent i = new Intent(getApplicationContext(), TopUp.class);
		             	startActivity(i);
		        		dialog.dismiss();
					}
				});
				dialogButtonNO.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						dialog.dismiss();
					}
				});	// TODO Auto-generated method stub
		
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

	private void btnClickedBus() 
	{
		buyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) 
			{
				    internetCheck = isOnline() ;
					
			    	if(internetCheck)
			    	{
			    		if(costOfJourney < userBalance)
			    		{
			    			busRoute2 = busRoute.getText().toString() ;//Assign values from Autos Here
			    			busStopFrom2 = busStopFrom.getText().toString() ;//Assign values from Autos Here
			    			busStopTo2 = busStopTo.getText().toString() ;//Assign values from Autos Here
							new PurchaseBusTicket().execute(""); 
			    		}
			    		else
			    		{ 
			    			dialog.show();
			    		}
			    	}
			    	else
			    	{
			       	    Toast.makeText(getApplicationContext(), "No Internet Connection. Please check your network settings and try again.", Toast.LENGTH_SHORT).show();
			    	}
			} 
 
		});
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
	    				ticketType = "Adult";	    			
	                    break;
	                case R.id.radioChild: 
	    				ticketType = "Child";
	                    break;	      
	                }
	            }}); 
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
                             userBalance = Double.parseDouble(returnString);

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
        	mDialog.setMessage("Getting Balance...");
            mDialog.show();             
        }
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
            postParameters.add(new BasicNameValuePair("route", busRoute2));
            postParameters.add(new BasicNameValuePair("tickettype", ticketType));
            postParameters.add(new BasicNameValuePair("stopfrom", busStopFrom2));
            postParameters.add(new BasicNameValuePair("stopto", busStopTo2));
            postParameters.add(new BasicNameValuePair("cost", costBus.getText().toString()));

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
             }
             else if(resultBuy.contains("NoFunds"))
             {
             	Toast.makeText(getApplicationContext(), "Purchase Fail", Toast.LENGTH_SHORT).show();
             }
             else
             {
            	 Toast.makeText(getApplicationContext(), "No Result", Toast.LENGTH_SHORT).show();
             }          	
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
