package fyp.sbarcoe.tabsswipe;

import info.androidhive.tabsswipe.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Validate extends Fragment 
{
    public static ImageView qrCode;
	Button qrTest ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{

		View rootView = inflater.inflate(R.layout.fragment_validate, container, false);
		qrCode = (ImageView) rootView.findViewById(R.id.imageView1);
		return rootView;
		
		//qrTest = (Button) findViewById(R.id.qrTest);

		
	}

}
