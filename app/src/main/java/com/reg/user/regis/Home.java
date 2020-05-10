package com.reg.user.regis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

public class Home extends AppCompatActivity {

    private static final String TAG = "Home";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate:Starting");
        mSectionsPageAdapter= new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager)findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout= (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
    }

    private void setupViewPager(ViewPager viewPager){

        SectionsPageAdapter adapter=new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Scales");
        adapter.addFragment(new Tab2Fragment(), "Extra");
        adapter.addFragment(new Tab3Fragment(), "Music");
        adapter.addFragment(new Tab4Fragment(), "Stats");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
            //edit out if not works
            /*
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    String loginmsg=(String)msg.obj;
                    if(loginmsg.equals("NOTSUCCESS")) {
                        Intent intent = new Intent(getApplicationContext(), Signin.class);
                        intent.putExtra("LoginMessage", "Logged Out");
                        startActivity(intent);
                        removeDialog(0);
                    }
                }
            };
            */

            SharedPreferences sharedPrefs = getSharedPreferences("sharedprefs", 0);
            SharedPreferences.Editor editor=sharedPrefs.edit();
            editor.remove("name_dt");
            editor.remove("age_dt");
            editor.remove("gender_dt");
            editor.remove("weight_dt");
            //editor.remove("feet_dt");
            //editor.remove("inches_dt");

            editor.putBoolean("complete", false);
            editor.apply();
            /*Message myMessage=new Message();
            myMessage.obj="NOTSUCCESS";
            handler.sendMessage(myMessage);
            finish();*/

            Intent reg=new Intent(this, Signin.class);

            Toast.makeText(this, "Edit Profile Now", Toast.LENGTH_SHORT).show();
            startActivity(reg);
            finish();

            return true;
        }




        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
}