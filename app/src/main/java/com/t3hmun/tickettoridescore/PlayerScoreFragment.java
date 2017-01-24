package com.t3hmun.tickettoridescore;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerScoreFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this fragment.
     */
    private static final String ARG_COLOUR_ORDINAL = "section_number";
    private static final String ARG_CONFIG = "config";
    private ConfigData config;
    private Colours playerColour;
    private View rootView;
    private LinearLayout routePane;
    private LinearLayout ticketPane;
    private LinearLayout rootPane;
    private Activity activity;

    /**
     * Must have no parameters to allow fragment restore to work.
     * The FragmentManager will always persist the arguments bundle
     */
    public PlayerScoreFragment() {

    }

    /**
     * Returns a new instance of this fragment with the relevant data bundled in.
     */
    public static PlayerScoreFragment newInstance(Colours playerColour, ConfigData config) {
        PlayerScoreFragment fragment = new PlayerScoreFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLOUR_ORDINAL, playerColour.ordinal());
        args.putParcelable(ARG_CONFIG, config);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_main, container, false);
        this.routePane = (LinearLayout) rootView.findViewById(R.id.route_pane);
        this.ticketPane = (LinearLayout) rootView.findViewById(R.id.ticket_pane);
        this.rootPane = (LinearLayout) rootView.findViewById(R.id.root_pane);

        loadArgs();

        populateRoutes();

        return rootView;
    }

    private void populateRoutes() {
        SparseIntArray routeScores = config.getRouteScores();
        for (int i = 0; i < routeScores.size(); i++) {
            int cars = routeScores.keyAt(i);
            int routes = routeScores.get(cars);
            RouteScoreView rsv = new RouteScoreView(getContext());
            rsv.initNumbers(cars, routes);
            routePane.addView(rsv);
        }
    }

    private void loadArgs() {
        Bundle args = getArguments();
        this.playerColour = Colours.values()[args.getInt(ARG_COLOUR_ORDINAL)];
        this.config = args.getParcelable(ARG_CONFIG);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof Activity)) return;
        this.activity = (Activity) context;
    }
}



























