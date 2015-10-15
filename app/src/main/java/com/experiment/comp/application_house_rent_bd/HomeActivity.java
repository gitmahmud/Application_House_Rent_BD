package com.experiment.comp.application_house_rent_bd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



import com.experiment.comp.application_house_rent_bd.Adapter.ViewPagerAdapter;
import com.experiment.comp.application_house_rent_bd.Others.SlidingTabLayout;


import org.json.JSONException;


import android.support.v7.widget.Toolbar;


public class HomeActivity extends  ActionBarActivity {

    SharedPreferences prefs = null;
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = { "Recent","Area" };
    int Numboftabs = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        prefs = getSharedPreferences("com.experiment.comp.application_house_rent_bd", MODE_PRIVATE);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //getSupportActionBar().setIcon(R.drawable.);
        Titles[0] =getResources().getString(R.string.recent);
        Titles[1] = getResources().getString(R.string.area);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles,
                Numboftabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {

                return R.color.abc_primary_text_material_dark;
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        try {
            //Log.d("Shout","OKay1");
            new DataFetcher(this).fetchData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }






        @Override
        protected void onResume() {
            super.onResume();

            if (prefs.getBoolean("firstrun", true)) {
                // Do first run stuff here then set 'firstrun' as false
                //Intent intent = new Intent(this,HelperActivity.class);
                //startActivityForResult(intent,1);



                // using the following line to edit/commit prefs
                //prefs.edit().putBoolean("firstrun", false).commit();
            }

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_add){
            Intent intent = new Intent(this,UserADActivity.class);
            startActivityForResult(intent,100);

        }

        return super.onOptionsItemSelected(item);
    }


}
