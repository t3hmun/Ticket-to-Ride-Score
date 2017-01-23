package com.t3hmun.tickettoridescore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter sectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager viewPager;
    private HashMap<Colours, Integer> colorMapping;
    private HashMap<Colours, TextView> scores;
    private Colours currentPlayer;
    private ConfigData config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        config = intent.getParcelableExtra(ConfigActivity.BUNDLE_CONF);

        colorMapping = new HashMap<>(10);
        colorMapping.put(Colours.RED, ContextCompat.getColor(this, R.color.player_red));
        colorMapping.put(Colours.YELLOW, ContextCompat.getColor(this, R.color.player_yellow));
        colorMapping.put(Colours.GREEN, ContextCompat.getColor(this, R.color.player_green));
        colorMapping.put(Colours.BLUE, ContextCompat.getColor(this, R.color.player_blue));
        colorMapping.put(Colours.BLACK, ContextCompat.getColor(this, R.color.player_black));

        // Coming from C# the inability to easily convert to int[] is baffling.
        // C# generics and LINQ really change the way one thinks about this stuff.
        List<Colours> colours = new ArrayList<>();
        for (HashMap.Entry<Colours, Boolean> entry : config.getPlayers().entrySet()) {
            if (!entry.getValue()) continue;
            colours.add(entry.getKey());
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), colours);

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        scores = new HashMap<>(10);

        for (int i = 0; i < colours.size(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.coloured_tab, null);
            assert view != null;
            TextView label = (TextView) view.findViewById(R.id.label);
            TextView score = (TextView) view.findViewById(R.id.score);
            Colours tabColour = colours.get(i);
            label.setText(tabColour.toString());
            score.setText("0");
            scores.put(tabColour, score);
            view.setBackgroundColor(colorMapping.get(tabColour));
            // This is required to make the custom view fill the tab properly.
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            assert tab != null;
            tab.setCustomView(view);
            tab.setTag(tabColour);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentPlayer = (Colours) tab.getTag();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "CurrentPlayer = " + currentPlayer.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlayerScoreFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_COLOUR_ORDINAL = "section_number";

        public PlayerScoreFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlayerScoreFragment newInstance(Colours playerColour) {
            PlayerScoreFragment fragment = new PlayerScoreFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_COLOUR_ORDINAL, playerColour.ordinal());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            //noinspection unused
            Colours playerColour = Colours.values()[getArguments().getInt(ARG_COLOUR_ORDINAL)];
            // TODO: Use playerColour to make some things in this fragment coloured.

            
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Colours> colours;

        SectionsPagerAdapter(FragmentManager fm, List<Colours> colours) {
            super(fm);
            this.colours = colours;
        }

        /**
         * Called to create a fragment, not called after creation - cached inside.
         *
         * @param position Tab position.
         * @return Fragment displayed in tab.
         */
        @Override
        public Fragment getItem(int position) {
            return PlayerScoreFragment.newInstance(colours.get(position));
        }

        @Override
        public int getCount() {
            return colours.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return colours.get(position).toString();
        }
    }
}
