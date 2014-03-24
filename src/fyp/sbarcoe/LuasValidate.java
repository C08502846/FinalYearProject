package fyp.sbarcoe;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fyp.sbarcoe.DartPurchase.PurchasedartTicket;
import info.androidhive.tabsswipe.R;
import info.androidhive.tabsswipe.adapter.ImageLoader;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class LuasValidate extends Activity 
{
    Button validate ;
	String resultBuy,resultData, result2, stopName, returnString, returnString2, returnURL ;
	String retAdChi, retSinRet, retline, retFrom, retTo, retCost, retExp, response3, result3 ;
	Dialog mDialog;
	Boolean internetCheck ;

	static String image_url;
	public static ImageView image;
	int loader;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_luas_validate);
		validate = (Button) findViewById(R.id.validateLuasBtn);
		mDialog = new ProgressDialog(LuasValidate.this);        
		getActionBar().setDisplayHomeAsUpEnabled(true);

		validate.setOnClickListener(new View.OnClickListener() 
		{			
	        public void onClick(View v) 
	        {                 
	          new GetJourneyData().execute("");
	        }
	    });
    	internetCheck = isOnline() ;
    	if(internetCheck)
    	{
        	new GetImageName().execute("");
    	}
    	else
    	{
       	    Toast.makeText(getApplicationContext(), "No Internet Connection. Please check your network settings and try again.", Toast.LENGTH_SHORT).show();
    	}

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.luas_validate, menu);
		return true;
	}
	class GetJourneyData extends AsyncTask<String, Void, String> 
	{	

        @Override
        protected String doInBackground(String... params) 
        {
        	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

            // define the parameter        cardnumber, expmonth, expyear, cv
            postParameters.add(new BasicNameValuePair("email", getEmail()));

            String response3 = null;
           // boolean success = false ;
                
            // call executeHttpPost method passing necessary parameters 
            try 
             {            	
            	response3 = CustomHttpClient.executeHttpPost("http://sbarcoe.net23.net/Android/getLuasData.php", postParameters);                         
            	result3 = response3.toString();             	
              }
              catch (Exception e) 
              {
                   Log.e("log_tag","Error in http connection!!" + e.toString());               
              	   Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();                      
              }
			  return response3; 
        }
        @Override
        protected void onPostExecute(String result) 
        {          
        	 mDialog.dismiss();
        	 //String remoteBal = Get Balance from Remote
        	 // updateLocalBal() ;
        	 
        	//parse json data
        	 try
             {
            	 returnString = "";
                 JSONArray jArray = new JSONArray(result);
                 Log.i("tagconvertstr", "["+result3+"]");
                     for(int i=0;i<jArray.length();i++)
                     {
                             JSONObject json_data = jArray.getJSONObject(i);                            
                             retAdChi += json_data.getString("type1");
                             retSinRet += json_data.getString("type2");
                             retline += json_data.getString("line");
                             retFrom += json_data.getString("stopFrom");
                             retTo += json_data.getString("stopTo");
                             retCost += json_data.getString("cost");
                             retExp += json_data.getString("exptime");
                            // userBalance = Double.parseDouble(returnString);                            
                     }
                     
                Toast.makeText(getApplicationContext(), "Ticket Info:"+"\n" +retAdChi+"\n"+retSinRet+"\n"+retline+"\n", Toast.LENGTH_LONG).show();                      

              }
             catch(JSONException e)
             {
                     Log.e("log_tag", "Error parsing data "+e.toString());
             }    
            // userBal.setText("Current Balance: €"+returnString+"");
             //userBal.setText("Current Balance: €"+returnString+"");               	
        }

        @Override
        protected void onPreExecute() 
        {
        	retAdChi = "";
            retSinRet = "";
            retline = "";
            retFrom = "";
            retTo = "";
            retCost = "";
            retExp = "";

	          // Make Dialog
	         
        	((AlertDialog) mDialog).setMessage("Retrieving Ticket Details...");
            mDialog.show();             
        }
}
	class GetImageName extends AsyncTask<String, Void, String> 
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
            	response2 = CustomHttpClient.executeHttpPost("http://sbarcoe.net23.net/Android/getLuasTicket.php", postParameters);                         
            	result2 = response2.toString(); 
            	
              }
              catch (Exception e) 
              {
                   Log.e("log_tag","Error in http connection!!" + e.toString());               
              	   Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_SHORT).show();                      
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
            	 returnString2 = "";
                 JSONArray jArray = new JSONArray(result2);
                 Log.i("tagconvertstr", "["+result2+"]");
                     for(int i=0;i<jArray.length();i++)
                     {
                             JSONObject json_data = jArray.getJSONObject(i);                            
                             returnString2 +=  json_data.getString("ticketimg");
                             System.out.print("Returned: "+returnString2);                        	 
                     }  
                     image_url = image_url+returnString2 ;                     
               	     ImageLoader imgLoader = new ImageLoader(getApplicationContext());
                     imgLoader.DisplayImage(image_url, loader, image);
                	 //Toast.makeText(getApplicationContext(), "Image URL: "+image_url, Toast.LENGTH_SHORT).show();
              }
             catch(JSONException e)
             {
                     Log.e("log_tag", "Error parsing data "+e.toString());
             }         
             //userBal.setText("Current Balance: €"+returnString+"");               	
        }

        @Override
        protected void onPreExecute() 
        {
            loader = R.drawable.loader;
            image_url = "http://sbarcoe.net23.net/Android/phpqrcode/tickets/";
    		image = (ImageView) findViewById(R.id.imageLuas);

        	((AlertDialog) mDialog).setMessage("Retrieving Ticket...");
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
