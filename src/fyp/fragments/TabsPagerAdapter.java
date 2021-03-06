package info.androidhive.tabsswipe.adapter;

import fyp.sbarcoe.BuyTicket;
import fyp.sbarcoe.Favourites;
import fyp.sbarcoe.TopUpFragment;
import fyp.sbarcoe.Validate;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

public class TabsPagerAdapter extends FragmentPagerAdapter 
{

	public TabsPagerAdapter(FragmentManager fm) 
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Buy Ticket fragment activity
			return new BuyTicket();
		case 1:
			// TopUp fragment activity
			return new Validate();
		case 2:
			// Validate fragment activity
			return new TopUpFragment();


		}
		return null;	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
