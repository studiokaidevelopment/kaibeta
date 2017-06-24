package com.studiokai.kaibeta;


import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private FragmentTabHost fragmentTabHost;


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_news, container, false);

        fragmentTabHost = (FragmentTabHost) v.findViewById(R.id.tab_host);

        fragmentTabHost.setup(getContext(), getFragmentManager(), R.id.real_tab_content);

        Resources.Theme theme = getActivity().getTheme();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentTabHost.addTab(fragmentTabHost.newTabSpec("tab1").setIndicator(null,
                    getResources().getDrawable(R.drawable.ic_newsfeed,theme)),
                    NewsFragment.class, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentTabHost.addTab(fragmentTabHost.newTabSpec("tab2").setIndicator(null,
                    getResources().getDrawable(R.drawable.ic_calendar,theme)),
                    BookingFragment.class, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentTabHost.addTab(fragmentTabHost.newTabSpec("tab3").setIndicator(null,
                    getResources().getDrawable(R.drawable.ic_shopping,theme)),
                    ShoppingFragment.class, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentTabHost.addTab(fragmentTabHost.newTabSpec("tab4").setIndicator(null,
                    getResources().getDrawable(R.drawable.ic_info,theme)),
                    ContactsFragment.class, null);
        }

        return v;
    }

}
