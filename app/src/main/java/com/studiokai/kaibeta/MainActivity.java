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

    /*

    private static String TAG = MainActivity.class.getSimpleName();

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitle("Studio Kai");

        mNavItems.add(new NavItem("Team", "Who we are", R.drawable.ic_team));
        mNavItems.add(new NavItem("Artists", "Who works with us", R.drawable.ic_artists));
        mNavItems.add(new NavItem("Services", "What we can do for you", R.drawable.ic_services));
        mNavItems.add(new NavItem("Affiliates", "Who we partner with", R.drawable.ic_affiliates));
        mNavItems.add(new NavItem("Contact", "Have a word with us", R.drawable.ic_contact));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter mAdapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(mAdapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });
    }

    private void selectItemFromDrawer(int position) {

        Fragment fragment;

        switch (position) {
            case 0 :
                fragment = new TeamFragment();
                break;
            case 1 :
                fragment = new PreferencesFragment();
                break;
            default:
                fragment = new PreferencesFragment();
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mainContent, fragment)
                .commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.about) {
            Toast.makeText(this, "Created by Just Titus", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    */


}
