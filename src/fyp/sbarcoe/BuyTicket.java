package fyp.sbarcoe;

import info.androidhive.tabsswipe.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class BuyTicket extends Fragment 
{
	ImageButton dubBus, luas, dart ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_buy, container, false);   
		dubBus = (ImageButton) rootView.findViewById(R.id.dubBus);
		luas = (ImageButton) rootView.findViewById(R.id.luas);
		dart = (ImageButton) rootView.findViewById(R.id.dart);
		
		dubBus.setOnClickListener(new View.OnClickListener() {public void onClick(View v)
		{			
			Intent i = new Intent(getActivity(), BusPurchase.class);
		    startActivity(i);
		}}); 
		luas.setOnClickListener(new View.OnClickListener() {public void onClick(View v)
		{			
			Intent i = new Intent(getActivity(), LuasPurchase.class);
			startActivity(i);
		}}); 
		dart.setOnClickListener(new View.OnClickListener() {public void onClick(View v)
		{			
			Intent i = new Intent(getActivity(), DartPurchase.class);
			startActivity(i);
		}}); 
		//luas.setOnClickListener(new View.OnClickListener() {public void onClick(View v){System.out.println("Luas");}}); 
		//dart.setOnClickListener(new View.OnClickListener() {public void onClick(View v){System.out.println("Dart");}}); 				
		return rootView;
	}
	
}
