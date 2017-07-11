package com.studiokai.kaibeta;

import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.View;
import android.widget.TabWidget;

public class MainActivity extends FragmentActivity {

    private FragmentTabHost fragmentTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent bookingIntent = new Intent(this, BookingActivity.class);
//        startActivity(bookingIntent);

        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
    }

    @Override
    protected void onStart() {

        super.onStart();

        fragmentTabHost.setup(getBaseContext(), getSupportFragmentManager(), R.id.realtabcontent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentTabHost.addTab(fragmentTabHost.newTabSpec("tab1").setIndicator(null,
                    getResources().getDrawable(R.drawable.ic_newsfeed,getTheme())),
                    NewsFragment.class, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentTabHost.addTab(fragmentTabHost.newTabSpec("tab2").setIndicator(null,
                    getResources().getDrawable(R.drawable.ic_calendar,getTheme())),
                    BookingFragment.class, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentTabHost.addTab(fragmentTabHost.newTabSpec("tab3").setIndicator(null,
                    getResources().getDrawable(R.drawable.ic_shopping,getTheme())),
                    ShoppingFragment.class, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentTabHost.addTab(fragmentTabHost.newTabSpec("tab4").setIndicator(null,
                    getResources().getDrawable(R.drawable.ic_info,getTheme())),
                    ContactsFragment.class, null);
        }

        TabWidget widget = fragmentTabHost.getTabWidget();

        for (int i = 0; i < widget.getChildCount(); i++) {
            View tabView = widget.getChildAt(i);
        }
    }
}
