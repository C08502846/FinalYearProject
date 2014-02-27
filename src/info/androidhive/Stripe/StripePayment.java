package info.androidhive.Stripe;

import info.androidhive.tabsswipe.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import com.stripe.android.*;

public class StripePayment extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stripe_payment);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stripe_payment, menu);
		return true;
	}

}
