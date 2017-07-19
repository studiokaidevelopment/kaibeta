package com.studiokai.kaibeta.newsfeed;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studiokai.kaibeta.MediaFragment;
import com.studiokai.kaibeta.R;

public class NewsFragment extends Fragment {

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_news, container, false);

        FragmentTabHost fragmentTabHost = (FragmentTabHost) v.findViewById(R.id.tab_host);
        fragmentTabHost.setup(getContext(), getChildFragmentManager(), R.id.real_tab_content);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentTabHost.addTab(fragmentTabHost.newTabSpec("tab1").setIndicator("News"),
                    NewsFeedFragment.class, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentTabHost.addTab(fragmentTabHost.newTabSpec("tab2").setIndicator("Media"),
                    MediaFragment.class, null);
        }

        return v;
    }

}
