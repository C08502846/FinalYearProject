package fyp.sbarcoe.tabsswipe;

import java.util.Random;

import info.androidhive.tabsswipe.R;
import info.androidhive.tabsswipe.R.layout;
import info.androidhive.tabsswipe.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Welcome extends Activity 
{
    Button continueB ; 
    TextView id, id2 ; 
    int idGen ;
    String idGenStr ;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		id = (TextView) findViewById(R.id.tvID);
		id2 = (TextView) findViewById(R.id.tvIDis);
		
		idGen = createID() ;
		checkIfExists() ;
		registerUser() ;
		
		StringBuilder sb = new StringBuilder();
		sb.append("");
		sb.append(idGen);
		idGenStr = sb.toString();
		System.out.println(idGenStr) 
		;
		//id.setText(idGenStr);
		id2.append(idGenStr);
		continueB = (Button) findViewById(R.id.continueB);	
		continueB.setOnClickListener(new View.OnClickListener() 
		{
            public void onClick(View v) 
            {
            	final Intent i = new Intent(getApplicationContext(), MainActivity.class);
    		    startActivity(i);
            }
        });
		
	}

	private void registerUser() 
	{
		// TODO Auto-generated method stub		
	}

	private void checkIfExists() 
	{
		// TODO Auto-generated method stub		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}
	public int createID()
	{
		Random r = new Random();
		int i1 = r.nextInt(10000-0);
		return i1;		
	}

}
