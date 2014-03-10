package fyp.sbarcoe.tabsswipe;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fyp.sbarcoe.tabsswipe.TopUp.deductFromCard;

import info.androidhive.tabsswipe.R;
import info.androidhive.tabsswipe.adapter.ImageLoader;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Validate extends Fragment 
{
    public static ImageView image, loader;
	Button qrTest ;
	static String image_url;
	String result, returnString ;
	ProgressDialog mDialog ;
	static TextView userBal ;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
        int loader = R.drawable.loader;
        image_url = "http://sbarcoe.net23.net/Android/phpqrcode/tickets/";
		mDialog = new ProgressDialog(getActivity());
    	//new GetImageName().execute("");  

		View rootView = inflater.inflate(R.layout.fragment_validate, container, false);
		image = (ImageView) rootView.findViewById(R.id.image);
		
		ImageLoader imgLoader = new ImageLoader(getActivity());
        imgLoader.DisplayImage(image_url, loader, image);
	        
	        // Imageview to show	        
	        // Image url
	        
	        // ImageLoader class instance
	        
	        // whenever you want to load an image from url
	        // call DisplayImage function
	        // url - image url to load
	        // loader - loader image, will be displayed before getting image
	        // image - ImageView 
			return rootView;		
	}
	public void getQRImage(String email)
	{
		//Query DB. Select ticketimg from LuasJourneys Where Email = $email
		// Pass back here and append string to url
		// Call display image with new url
		
	}
	
	private String getEmail() 
	{
		DBManager myDB = new DBManager(getActivity());
		myDB.open();	
		String emailReturn ;
		emailReturn = myDB.getEmail();	
		myDB.close();
		return emailReturn;
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
            	response2 = CustomHttpClient.executeHttpPost("http://sbarcoe.net23.net/Android/getQRCodeImg.php", postParameters);                         
            	result = response2.toString(); 
            	
              }
              catch (Exception e) 
              {
                   Log.e("log_tag","Error in http connection!!" + e.toString());               
              	   Toast.makeText(getActivity(), "No Internet Connection.", Toast.LENGTH_SHORT).show();                      
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
                 Log.i("tagconvertstr", "["+result+"]");
                     for(int i=0;i<jArray.length();i++)
                     {
                             JSONObject json_data = jArray.getJSONObject(i);                            
                             returnString +=  json_data.getString("ticketimg");
                             System.out.print("Returned: "+returnString);                        	 
                     }  
                     image_url.concat(returnString);
                	 Toast.makeText(getActivity(), "Image URL: "+image_url, Toast.LENGTH_SHORT).show();                      

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
        	mDialog.setMessage("Retrieving Ticket...");
            mDialog.show();             
        }
}

}
