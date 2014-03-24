package fyp.sbarcoe.tabsswipe;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.tabsswipe.R;
import info.androidhive.tabsswipe.adapter.ImageLoader;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Validate extends Fragment 
{
    public static ImageView image, loader;
	Button qrTest ;
	String result, returnString ;
	ProgressDialog mDialog ;
	static TextView userBal ;
	
	ImageButton dubBus, luas, dart ;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		mDialog = new ProgressDialog(getActivity());
		View rootView = inflater.inflate(R.layout.fragment_validate, container, false); 
		
		dubBus = (ImageButton) rootView.findViewById(R.id.valDBusBtn);
		luas = (ImageButton) rootView.findViewById(R.id.valLuasBtn);
		dart = (ImageButton) rootView.findViewById(R.id.valDartBtn);
		
		dubBus.setOnClickListener(new View.OnClickListener() {public void onClick(View v)
		{			
			Intent i = new Intent(getActivity(), BusValidate
					.class);
		    startActivity(i);
		}}); 
		luas.setOnClickListener(new View.OnClickListener() {public void onClick(View v)
		{			
			Intent i = new Intent(getActivity(), LuasValidate.class);
			startActivity(i);
		}}); 
		dart.setOnClickListener(new View.OnClickListener() {public void onClick(View v)
		{			
			Intent i = new Intent(getActivity(), DartValidate.class);
			startActivity(i);
		}}); 
		
		return rootView;	
		
		

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
	

}
