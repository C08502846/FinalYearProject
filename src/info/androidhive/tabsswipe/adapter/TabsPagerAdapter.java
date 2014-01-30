package info.androidhive.tabsswipe.adapter;

import fyp.sbarcoe.tabsswipe.BuyTicket;
import fyp.sbarcoe.tabsswipe.Favourites;
import fyp.sbarcoe.tabsswipe.TopUp;
import fyp.sbarcoe.tabsswipe.Validate;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter 
{

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new BuyTicket();
		case 1:
			// Games fragment activity
			return new TopUp();
		case 2:
			// Movies fragment activity
			return new Validate();
		case 3:
			// Movies fragment activity
			return new Favourites();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}
