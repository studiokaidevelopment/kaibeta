package com.studiokai.kaibeta;


import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

        fragmentTabHost.setup(getContext(), getChildFragmentManager(), R.id.real_tab_content);

        Resources.Theme theme = getActivity().getTheme();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentTabHost.addTab(fragmentTabHost.newTabSpec("tab1").setIndicator("News"),
                    NewsFeedFragment.class, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentTabHost.addTab(fragmentTabHost.newTabSpec("tab2").setIndicator("Media"),
                    MediaFragment.class, null);
        }

        KaiFacebook kaiFacebook = new KaiFacebook();
        kaiFacebook.loadPosts();

        return v;
    }

}
