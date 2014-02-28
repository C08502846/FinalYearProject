package fyp.sbarcoe.tabsswipe;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import info.androidhive.tabsswipe.R;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TopUp extends Fragment implements OnItemSelectedListener
{
	String topUpAmt ;
	Spinner topUp ;
	TextView userBal ;
	Button topUpButton ;
	int topUpInt ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{		
		View rootView = inflater.inflate(R.layout.fragment_topup, container, false);	
	
		topUp = (Spinner) rootView.findViewById(R.id.spinnerTopUp);				
		topUp.setOnItemSelectedListener(this);
		topUpButton = (Button) rootView.findViewById(R.id.bTopUp);
		ArrayAdapter<CharSequence> topUpAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.top_up, android.R.layout.simple_spinner_item);	
		
		topUpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		topUp.setAdapter(topUpAdapter);	
		userBal = (TextView) rootView.findViewById(R.id.tvTopUpBal);



		topUpButton.setOnClickListener(new View.OnClickListener() {public void onClick(View v)
		{				
			String localEmail = getEmail();
	   	  //Toast.makeText(getActivity(), "Email: "+localEmail, Toast.LENGTH_SHORT).show();	   	    
	   	    //deductFunds(localEmail, topUpInt);
        	new deductFromCard().execute("");  

	   	   // topUpUser() ;

		}

		});
		
		return rootView;
	}
	class deductFromCard extends AsyncTask<String, Void, String> 
	{
		
		private ProgressDialog mDialog = new ProgressDialog(getActivity());

        @Override
        protected String doInBackground(String... params) 
        {
        	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        	StringBuilder sb = new StringBuilder();
    		sb.append("");
    		sb.append(topUpAmt);
    		String topUpAmtConv = sb.toString();
    				
            // define the parameter        cardnumber, expmonth, expyear, cv
            postParameters.add(new BasicNameValuePair("email", getEmail()));
            postParameters.add(new BasicNameValuePair("topUpAmt", topUpAmtConv));

            String response = null;
            String result = null ;
           // boolean success = false ;
                
            // call executeHttpPost method passing necessary parameters 
            try 
             {
            	response = CustomHttpClient.executeHttpPost("http://sbarcoe.net23.net/Android/deductCard.php", postParameters);                         
            	result = response.toString();  
              }
              catch (Exception e) 
              {
                   Log.e("log_tag","Error in http connection!!" + e.toString());               
              	   Toast.makeText(getActivity(), "No Internet Connection.", Toast.LENGTH_SHORT).show();                      
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
             	Toast.makeText(getActivity(), "You have topped up by "+"€"+topUpAmt+"", Toast.LENGTH_SHORT).show();
             	
             	
             	//insertLocalUserData(email.getText().toString(), pw.getText().toString());
             	
             	//final Intent i = new Intent(getActivity(), MainActivity.class);
             	//startActivity(i);
             }
             else if(result.contains("NoFunds"))
             {
             	Toast.makeText(getActivity(), "Not enough funds", Toast.LENGTH_SHORT).show();
             }
             else
             {
            	 Toast.makeText(getActivity(), "No Result.", Toast.LENGTH_SHORT).show();
             }
        }

        @Override
        protected void onPreExecute() 
        {
        	mDialog.setMessage("Registering Card Details...");
            mDialog.show();             
        }


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
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
	{
		topUpAmt = topUp.getSelectedItem().toString();
		topUpInt = Integer.parseInt(topUpAmt);	
   	    //Toast.makeText(getActivity(), "Top Up Is: "+topUpInt, Toast.LENGTH_SHORT).show();                      

		
		//int year1 = Integer.parseInt(month);	
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

		// deduct from card topUpInt if card balance > topUpInt
		//(Update cards SET Balance = Balance - topUpInt
	    //WHERE Email = post(email)		


}
