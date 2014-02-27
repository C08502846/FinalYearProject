package fyp.sbarcoe.tabsswipe;

import info.androidhive.tabsswipe.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class TopUp extends Fragment implements OnItemSelectedListener
{
	String topUpAmt ;
	Spinner topUp ;
	TextView testData ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{		
		View rootView = inflater.inflate(R.layout.fragment_topup, container, false);	
	
		topUp = (Spinner) rootView.findViewById(R.id.spinnerTopUp);				
		topUp.setOnItemSelectedListener(this);

		ArrayAdapter<CharSequence> topUpAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.top_up, android.R.layout.simple_spinner_item);	
		
		topUpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		topUp.setAdapter(topUpAdapter);	
		
    	//DBManager myDB = new DBManager(getActivity());
		//myDB.open();
		//String localEmail = testData.getText().toString();
		//localEmail = myDB.getEmail();	
   	    //Toast.makeText(getActivity(), "Email: "+localEmail, Toast.LENGTH_SHORT).show();
		//myDB.close();
		
		return rootView;
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
	{
		topUpAmt = topUp.getSelectedItem().toString();
		int topUpInt = Integer.parseInt(topUpAmt);
   	    //Toast.makeText(getActivity(), "Top Up Is: "+topUpInt, Toast.LENGTH_SHORT).show();                      

		
		//int year1 = Integer.parseInt(month);	
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
