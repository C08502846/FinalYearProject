package fyp.sbarcoe.tabsswipe;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import info.androidhive.tabsswipe.R;
import info.androidhive.tabsswipe.adapter.ImageLoader;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class BusValidate extends Activity 
{
    Button validate ;
	String resultBuy, result2 ;
	String stopName, returnString, returnString2, returnURL ;
	Dialog mDialog;
	
	static String image_url;
	public static ImageView image;
	int loader;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_validate);
		validate = (Button) findViewById(R.id.validateBusBtn);
		mDialog = new ProgressDialog(BusValidate.this);        
		getActionBar().setDisplayHomeAsUpEnabled(true);

		validate.setOnClickListener(new View.OnClickListener() 
		{			
	        public void onClick(View v) 
	        {                 
	          Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();	                                                 	         	    
	        }
	    });
    	new GetImageName().execute("");

	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dart_validate, menu);
		return true;
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
            	response2 = CustomHttpClient.executeHttpPost("http://sbarcoe.net23.net/Android/getBusTicket.php", postParameters);                         
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
    		image = (ImageView) findViewById(R.id.imageBus);

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
