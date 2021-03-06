package com.t3hmun.tickettoridescore;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements PlayerScoreFragment.PlayerScoreChangeListener {

    public static final String SCORES = "scores";
    public static final String COLOURS = "colours";
    private Map<Colours, Integer> colorMapping;
    private Map<Colours, TextView> scoreTextViews;
    private Map<Colours, Integer> scores;
    private ConfigData config;
    private List<Colours> chosenPlayerColours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        config = intent.getParcelableExtra(ConfigActivity.BUNDLE_CONF);


        colorMapping = initColourMapping();
        chosenPlayerColours = loadChosenPlayerColours();

        scores = new HashMap<>(10);

        if (savedInstanceState != null) {
            int[] scoresNums = savedInstanceState.getIntArray(SCORES);
            int[] colours = savedInstanceState.getIntArray(COLOURS);
            Colours[] coloursOrdinals = Colours.values();


            assert scoresNums != null;
            for (int i = 0; i < scoresNums.length; i++) {
                assert colours != null;
                scores.put(coloursOrdinals[colours[i]], scoresNums[i]);
            }
        } else {
            for (final Colours player : chosenPlayerColours) {
                scores.put(player, 0);
            }
        }

        TabLayout tabLayout = initToolbarTabsAndPager();
        scoreTextViews = initCustomTabs(chosenPlayerColours, tabLayout);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Bundle map into 2 arrays.
        int size = scores.size();
        int[] scoresNums = new int[size];
        int[] colours = new int[size];
        Set<Map.Entry<Colours, Integer>> entries = scores.entrySet();
        int i = 0;
        for (Map.Entry<Colours, Integer> entry : entries) {
            scoresNums[i] = entry.getValue();
            colours[i] = entry.getKey().ordinal();
            i++;
        }

        outState.putIntArray(SCORES, scoresNums);
        outState.putIntArray(COLOURS, colours);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Abandon Game?")
                .setMessage("Do you want to abandon this game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @NonNull
    private TabLayout initToolbarTabsAndPager() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //  Has no use at the moment.
                //currentPlayer = (Colours) tab.getTag();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return tabLayout;
    }

    @NonNull
    private List<Colours> loadChosenPlayerColours() {
        // Coming from C# the inability to easily convert to int[] is baffling.
        // C# generics and LINQ really change the way one thinks about this stuff.
        List<Colours> chosenPlayerColours = new ArrayList<>();
        for (HashMap.Entry<Colours, Boolean> entry : config.getPlayers().entrySet()) {
            if (!entry.getValue()) continue;
            chosenPlayerColours.add(entry.getKey());
        }
        return chosenPlayerColours;
    }

    private Map<Colours, Integer> initColourMapping() {
        Map<Colours, Integer> colorMap = new HashMap<>(10);
        colorMap.put(Colours.RED, ContextCompat.getColor(this, R.color.player_red));
        colorMap.put(Colours.YELLOW, ContextCompat.getColor(this, R.color.player_yellow));
        colorMap.put(Colours.GREEN, ContextCompat.getColor(this, R.color.player_green));
        colorMap.put(Colours.BLUE, ContextCompat.getColor(this, R.color.player_blue));
        colorMap.put(Colours.BLACK, ContextCompat.getColor(this, R.color.player_black));
        return colorMap;
    }

    private Map<Colours, TextView> initCustomTabs(List<Colours> colours, TabLayout tabLayout) {
        Map<Colours, TextView> scoreViews = new HashMap<>(10);
        for (int i = 0; i < colours.size(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.coloured_tab, null);
            assert view != null;
            TextView label = (TextView) view.findViewById(R.id.label);
            TextView score = (TextView) view.findViewById(R.id.score);
            Colours tabColour = colours.get(i);
            label.setText(tabColour.toString());
            score.setText(String.format(Locale.getDefault(), "%d", scores.get(tabColour)));
            scoreViews.put(tabColour, score);
            view.setBackgroundColor(colorMapping.get(tabColour));
            // This is required to make the custom view fill the tab properly.
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            assert tab != null;
            tab.setCustomView(view);
            tab.setTag(tabColour);
        }
        return scoreViews;
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
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ScoreChanged(PlayerData data) {
        SparseIntArray routeScores = config.getRouteScores();
        int score = data.calcuateScore(routeScores);
        TextView scoreText = scoreTextViews.get(data.getPlayerColour());
        String text = String.format(Locale.getDefault(), "%d", score);
        scores.put(data.getPlayerColour(), score);
        scoreText.setText(text);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        private final List<Colours> playerColours;

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            // Adding this field makes the code neater.
            playerColours = chosenPlayerColours;

        }

        /**
         * Called to create a fragment, not called after creation - cached inside.
         * Not called on screen rotation either because the FragmentManager does things.
         *
         * @param position Tab position.
         * @return Fragment displayed in tab.
         */
        @Override
        public Fragment getItem(int position) {
            Colours playerColour = playerColours.get(position);
            return PlayerScoreFragment.newInstance(playerColour, config, colorMapping.get(playerColour));
        }

        @Override
        public int getCount() {
            return playerColours.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return playerColours.get(position).toString();
        }
    }
}
