package com.t3hmun.tickettoridescore;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerScoreFragment extends Fragment {

    private static final String ARG_CONFIG = "config";
    private static final String ARG_DATA = "data";
    private ConfigData config;
    private View rootView;
    private LinearLayout routePane;
    private LinearLayout ticketPane;
    private LinearLayout rootPane;
    private Activity activity;
    private PlayerData data;

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
        PlayerData data = new PlayerData(playerColour);
        args.putParcelable(ARG_DATA, data);
        args.putParcelable(ARG_CONFIG, config);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        routePane = (LinearLayout) rootView.findViewById(R.id.route_pane);
        ticketPane = (LinearLayout) rootView.findViewById(R.id.ticket_pane);
        rootPane = (LinearLayout) rootView.findViewById(R.id.root_pane);

        loadArgs();

        initRouteViews();

        return rootView;
    }

    private void initRouteViews() {
        SparseIntArray routeScores = config.getRouteScores();
        final SparseIntArray routeData = data.getRoutes();
        for (int i = 0; i < routeScores.size(); i++) {
            final int cars = routeScores.keyAt(i);
            final int points = routeScores.get(cars);
            final RouteScoreView rsv = new RouteScoreView(getContext());
            rsv.initNumbers(cars, points);
            if (routeData.get(cars, -1) == -1) {
                routeData.put(cars, 0);
            }
            rsv.setQuantity(routeData.get(cars));

            rsv.setOnChangeListener(new RouteScoreView.ScoreChangeListener() {
                @Override
                public void onChange(boolean increment) {
                    int val = routeData.get(cars) + (increment ? 1 : -1);
                    if (val < 0) val = 0;
                    routeData.put(cars, val);
                    rsv.setQuantity(routeData.get(cars));
                }
            });

            routePane.addView(rsv);
        }
    }

    private void loadArgs() {
        Bundle args = getArguments();
        config = args.getParcelable(ARG_CONFIG);
        data = args.getParcelable(ARG_DATA);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof Activity)) return;
        activity = (Activity) context;
    }

    @Override
    public void onPause() {
        // Update data in args.
        // Config does not change so that is not updated.
        getArguments().putParcelable(ARG_DATA, data);
        super.onPause();
    }
}



























