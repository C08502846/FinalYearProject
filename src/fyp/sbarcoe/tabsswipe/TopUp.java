package fyp.sbarcoe.tabsswipe;

import info.androidhive.tabsswipe.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class TopUp extends Fragment 
{
	
	Spinner topUp ;
	TextView testData ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{		
		View rootView = inflater.inflate(R.layout.fragment_topup, container, false);	
	
		topUp = (Spinner) rootView.findViewById(R.id.spinnerTopUp);				
		
		ArrayAdapter<CharSequence> topUpAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.top_up, android.R.layout.simple_spinner_item);	
		
		topUpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		topUp.setAdapter(topUpAdapter);	
		
		return rootView;
	}
}
