package com.tagworld.slidingmenu;

import com.iapp.playdate.Add_Child;
import com.iapp.playdate.Arrange_date_fragment;
import com.iapp.playdate.DrawerItem;
import com.iapp.playdate.Home;
import com.iapp.playdate.IntentChoserBuilder;
import com.iapp.playdate.R;
import com.iapp.playdate.Send_mail;
import com.iapp.playdate.Sets;
import com.iapp.playdate.authentication;
import com.iapp.playdate.friendlist;
import com.iapp.playdate.notification_fragement;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SampleListFragment extends ListFragment {
	
	String view_data;
	int count;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		SampleAdapter adapter = new SampleAdapter(getActivity());
			adapter.add(new SampleItem("Arrange", R.drawable.add_icon));
			adapter.add(new SampleItem("Add Child", R.drawable.child_icon));
			adapter.add(new SampleItem("Sets", R.drawable.sets_icon));
			adapter.add(new SampleItem("Friends", R.drawable.friends_icon));
			adapter.add(new SampleItem("Guardians", R.drawable.upgrade_icon));
			adapter.add(new SampleItem("Notifications", R.drawable.notification_icon));
			adapter.add(new SampleItem("Invite", R.drawable.invite_icon));
			adapter.add(new SampleItem("feedback", R.drawable.feedback_icon));
		    setListAdapter(adapter);

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		Fragment fragment = null;
		
		switch (position) {
		case 0: {
			
			fragment = new Arrange_date_fragment();
			
			break;
		}
		case 1: {
			fragment = new Add_Child();
			
			break;
		}
		case 2: 
		{
			fragment = new Sets();
			
			break;
		}
		case 3: {
			Bundle bundle = new Bundle();
			bundle.putString("response_data", "");
			bundle.putString("check", "1");
			
			fragment= new friendlist();
			fragment.setArguments(bundle);
		

		}
			break;
		case 4: {
		 	fragment = new authentication();
		
			break;
		}
		case 5: {
			Bundle bundle = new Bundle();
			
				bundle.putString("view",view_data );
				fragment=new notification_fragement();
				fragment.setArguments(bundle);
				count++;
		

		}
			break;
		case 6:

		{
//			Resources resources = getResources();
//			Intent shareIntent = new Intent(Intent.ACTION_SEND);
//			shareIntent.setType("text/plain");
//			shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out Playdate by JAGO for the fastest and simplest way to schedule kids playdates  www.jago.nu");
//			shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Fast Playdate Planning from JAGO.");
//			PackageManager pm = getActivity().getPackageManager();
//			IntentChoserBuilder.createChoserIntent(getActivity(), shareIntent, pm, "Check out Playdate by JAGO for the fastest and simplest way to schedule kids playdates  www.jago.nu", "Fast Playdate Planning from JAGO.");	
		
		}

			break;
		case 7: {
			fragment = new Send_mail();
	
			break;
		}
		

		default:
			break;
		
		}
		if (fragment != null)
			switchFragment(fragment);
	}

	private class SampleItem {
		public String tag;
		public int iconRes;

		public SampleItem(String tag, int iconRes) {
			this.tag = tag;
			this.iconRes = iconRes;
		}
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
		}

	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(final int position, View view, ViewGroup parent) {
			if (view == null) {
				view = LayoutInflater.from(getContext()).inflate(R.layout.row,
						null);
			}
			ImageView icon = (ImageView) view.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			final TextView title = (TextView) view.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);

			return view;
		}
	}
}
