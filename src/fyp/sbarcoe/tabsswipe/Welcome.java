package fyp.sbarcoe.tabsswipe;

import info.androidhive.tabsswipe.R;
import info.androidhive.tabsswipe.R.layout;
import info.androidhive.tabsswipe.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Welcome extends Activity 
{
    Button continueB ; 
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
//		continueB = (Button) findViewById(R.id.continueB);		
//		continueB.setOnClickListener(new View.OnClickListener() 
//		{
//            public void onClick(View v) 
//            {
//            	final Intent i = new Intent(getApplicationContext(), MainActivity.class);
//    		    startActivity(i);
//            }
//        });
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

}
