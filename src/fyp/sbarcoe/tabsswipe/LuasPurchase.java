package fyp.sbarcoe.tabsswipe;

import fyp.sbarcoe.tabsswipe.BusPurchase.GetBal;
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
import android.widget.Button;
import android.widget.ImageView;
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
	private RadioGroup radioLineGroup, radioType1Group, radioType2Group;
	private RadioButton radioLineButton, radioType1Button, radioType2Button;
	int zoneFromInt, zoneToInt, zoneDiff ;
	double costOfJourney, userBalance;	
	String line, returnString, resultBal, resultBuy, resultQR, fromStop, toStop, zoneFrom, zoneTo, totalCost, type1, type2 ;
	String[] result; 
	TextView userBal, costLuas ;	
	Button btnBuy, dialogButtonOK, dialogButtonNO ;
	ProgressDialog mDialog ;
	Boolean internetCheck ;
	Dialog dialog;
	Context context ;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_luas_purchase);
		context = this;
		luasFrom = (Spinner) findViewById(R.id.spinnerLuasFrom);
		luasTo = (Spinner) findViewById(R.id.spinnerLuasTo); 
		costLuas = (TextView) findViewById(R.id.tvCostLuas);
		userBal = (TextView) findViewById(R.id.userBal);
		btnBuy = (Button) findViewById(R.id.btnBuyLuasTick);

		mDialog = new ProgressDialog(LuasPurchase.this);
		
		addRadioGroupListeners();
		setSpinnerListeners() ;
		btnClicked() ;
		setUpDialogs();
		
		zoneDiff = 0 ;
		line = "Green";
		type1 = "Return";
		type2 = "Child" ;
		
		result = populateStops("Green");
		luas_adp = new ArrayAdapter<String> (getApplicationContext(),android.R.layout.simple_dropdown_item_1line,result);
		luas_adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		luasTo.setAdapter(luas_adp);
	    luasFrom.setAdapter(luas_adp);
	    
        internetCheck = isOnline() ;		
    	if(internetCheck)
    	{
    		new GetBal().execute(""); 
    	}
    	else
    	{
       	    Toast.makeText(getApplicationContext(), "No Internet Connection. Please check your network settings and try again.", Toast.LENGTH_SHORT).show();
    	}
	}
	@Override
	protected void onResume() 
	{
	    super.onResume();
		new GetBal().execute(""); 
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
				    internetCheck = isOnline() ;
					
			    	if(internetCheck)
			    	{
			    		if(costOfJourney < userBalance)
			    		{
							new PurchaseTicket().execute(""); 
			    		}
			    		else
			    		{ 
			    			dialog.show();
			    		}
			    	}	
			} 
		});
	}
	public void addRadioGroupListeners() 
	 {		 
			radioLineGroup = (RadioGroup) findViewById(R.id.radioColor);
			int selectedId = radioLineGroup.getCheckedRadioButtonId();
			radioLineButton = (RadioButton) findViewById(selectedId);
			
			radioType1Group = (RadioGroup) findViewById(R.id.radioSinRet);
			int selectedId1 = radioType1Group.getCheckedRadioButtonId();
			radioType1Button = (RadioButton) findViewById(selectedId1);
			
			radioType2Group = (RadioGroup) findViewById(R.id.radioADCHI);
			int selectedId2 = radioType2Group.getCheckedRadioButtonId();
			radioType2Button = (RadioButton) findViewById(selectedId2);
			
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
	                	costLuas.setText("Journey Cost: €");	                	
	    				result = populateStops("Green");	
	    				line = "Green";
	    				luas_adp = new ArrayAdapter<String> (getApplicationContext(),android.R.layout.simple_dropdown_item_1line,result);
	    				luas_adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    				luasTo.setAdapter(luas_adp);
	    			    luasFrom.setAdapter(luas_adp);
	                    break;
	                case R.id.radioRed:	   
	            		costLuas.setText("Journey Cost: €");	               
	    				result = populateStops("Red");
	    				line = "Red";
	    				luas_adp = new ArrayAdapter<String> (getApplicationContext(),android.R.layout.simple_dropdown_item_1line,result);
	    				luas_adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    				luasTo.setAdapter(luas_adp);
	    			    luasFrom.setAdapter(luas_adp);   
	                    break;	      
	                }
	            }}); 
	        radioType1Group.setOnCheckedChangeListener(new OnCheckedChangeListener()
	        {
	        	@Override
	        	public void onCheckedChanged(RadioGroup group, int checkedId)
	        	{
	        		switch(checkedId)
	        		{
	        		case R.id.radioSingle:	
	        			type1 = "Single";
	        			break;
	        		case R.id.radioReturn:  
	        			type1 = "Return";	        			
	        			break;	      
	        		}
	        	}}); 
	        radioType2Group.setOnCheckedChangeListener(new OnCheckedChangeListener()
	        {
	        	@Override
	        	public void onCheckedChanged(RadioGroup group, int checkedId2)
	        	{
	        		switch(checkedId2)
	        		{
	        		case R.id.radioAdult:	
	        			type2 = "Adult";

	        			break;
	        		case R.id.radioChild:  
	        			type2 = "Child";

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
        	mDialog.setMessage("Updating Balance..");
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
            postParameters.add(new BasicNameValuePair("type1", type1));            
            postParameters.add(new BasicNameValuePair("type2", type2));            
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
           	    final Intent i = new Intent(getApplicationContext(), LuasValidate.class);
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
        	mDialog.setMessage("Purchasing Ticket...");
            mDialog.show();             
        }
    }
	
	public String getStopZone(String stopName)
	{
		 String result2 ;
		 DBManager myDB = new DBManager(this);
		 myDB.open();
	     result2 = myDB.getLuasZone(stopName) ;
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
		String[] result = myDB.getLuasStops(stopSelected) ;	
		//userBal = (TextView) findViewById(R.id.userBal);
		myDB.close(); 
		return result ;

	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
	{

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}