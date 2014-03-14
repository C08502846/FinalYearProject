package info.androidhive.tabsswipe.adapter;

import fyp.sbarcoe.tabsswipe.BuyTicket;
import fyp.sbarcoe.tabsswipe.Favourites;
import fyp.sbarcoe.tabsswipe.TopUpFragment;
import fyp.sbarcoe.tabsswipe.Validate;
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
			return new TopUpFragment();
		case 2:
			// Validate fragment activity
			return new Validate();

		}
		return null;	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
